package net.skidcode.gh.server.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.entity.Entity;
import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.protocol.PlaceBlockPacket;
import net.skidcode.gh.server.network.protocol.RemoveEntityPacket;
import net.skidcode.gh.server.network.protocol.UpdateBlockPacket;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.TickNextTickData;
import net.skidcode.gh.server.utils.Utils;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.chunk.Chunk;
import net.skidcode.gh.server.world.generator.BiomeSource;
import net.skidcode.gh.server.world.generator.LevelSource;
import net.skidcode.gh.server.world.generator.RandomLevelSource;

public class World {
	
	public HashMap<Integer, Player> players = new HashMap<>();
	public HashMap<Integer, Entity> entities = new HashMap<>();
	private static int freeEID = 1;
	public int worldSeed = 0x256512;
	public BedrockRandom random;
	public Chunk[][] chunks = new Chunk[16][16];
	public boolean instantScheduledUpdate = false, editingBlocks = false;
	public int spawnX, spawnY, spawnZ;
	public String name = "world";
	public long worldTime = 0;
	public int saveTime = 0;
	public int[][] locationTable;
	public int unknown5 = 0;
	public BiomeSource biomeSource;
	public LevelSource levelSource;
	public HashSet<Integer> blockUpdates = new HashSet<>();
	public int randInt1, randInt2;
	public int lightUpdatesCount = 0;
	public boolean updateLights = true;
	
	
	public TreeSet<TickNextTickData> scheduledTickTreeSet;
	public HashSet<TickNextTickData> scheduledTickSet;
	public int lightDepth = 0;
	public ArrayList<LightUpdate> lightUpdates;
	
	public World(int seed) {
		this.worldSeed = seed;
		this.random = new BedrockRandom(seed);
		this.biomeSource = new BiomeSource(this);
		this.levelSource = new RandomLevelSource(this, seed); //TODO API
		this.scheduledTickTreeSet = new TreeSet<>();
		this.scheduledTickSet = new HashSet<>();
		this.lightUpdates = new ArrayList<>();
		this.randInt1 = 0x283AE83; //it is static in 0.1
		this.randInt2 = 0x3C6EF35F;
	}
	
	public void addEntity(Entity entity) {
		entity.world = this;
		this.entities.put(entity.eid, entity);
	}
	public void setUpdateLights(boolean b) {
		this.updateLights = b;
	}
	public void addToTickNextTick(int x, int y, int z, int id, int delay) {
		TickNextTickData tick = new TickNextTickData(x, y, z, id);
		if(this.instantScheduledUpdate) {
			if(this.hasChunksAt(tick.posX - 8, tick.posY - 8, tick.posZ - 8, tick.posX + 8, tick.posY + 8, tick.posZ + 8)) {
				int worldID = this.getBlockIDAt(x, y, z);
				if(worldID == tick.blockID && worldID > 0) {
					Block.blocks[worldID].tick(this, x, y, z, this.random);
				}
			}
		}else if(this.hasChunksAt(x - 8, y - 8, z - 8, x + 8, y + 8, z + 8)){
			if(id > 0) tick.scheduledTime = delay + this.worldTime;
			if(!scheduledTickSet.contains(tick)) {
				scheduledTickSet.add(tick);
				scheduledTickTreeSet.add(tick);
			}
		}
	}

	public boolean hasChunksAt(int x, int y, int z, int radius) {
		return this.hasChunksAt(x - radius, y - radius, z - radius, x + radius, y + radius, radius + z);
	}
	public boolean hasChunksAt(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		
		if(maxY < 0 || minY > 127) return false;
		
		for(int chunkX = minX >> 4; chunkX <= maxX >> 4; ++chunkX) {
			for(int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; ++chunkZ) {
				if(chunkX < 0 || chunkZ < 0 || chunkX > 15 || chunkZ > 15 || this.chunks[chunkX][chunkZ] == null) return false;
			}
		}
		return true;
	}
	
	public void addPlayer(Player player) {
		this.players.put(player.eid, player);
	}
	
	public void removePlayer(int eid) {
		this.players.remove(eid);
		RemoveEntityPacket pk = new RemoveEntityPacket();
		pk.eid = eid;
		for(Player p : this.players.values()) {
			p.dataPacket(pk.clone());
		}
	}
	
	public void setSaveSpawn(int x, int z) {
		this.spawnX = x;
		this.spawnZ = z;
		Chunk c = this.getChunk(x >> 4, z >> 4);
		int cBX = x & 0xf;
		int cBZ = z & 0xf;
		for(int y = 125; y >= 0; --y) {
			int id = c.getBlockID(cBX, y, cBZ);
			if(id > 0) {
				Block b = Block.blocks[id];
				if(b.material.isSolid) {
					int idup = c.getBlockID(cBX, y + 1, cBZ);
					int idupper = c.getBlockID(cBX, y + 2, cBZ);
					if(idup == 0 && idupper == 0) { //TODO isSolid & isLiquid checks
						this.spawnY = y+2;
						return;
					}
					
				}
			}
		}
		this.spawnY = 127;
	}
	
	public void notifyNearby(int x, int y, int z, int cid) {
		this.notifyNeighbor(x - 1, y, z, cid);
		this.notifyNeighbor(x + 1, y, z, cid);
		this.notifyNeighbor(x, y - 1, z, cid);
		this.notifyNeighbor(x, y + 1, z, cid);
		this.notifyNeighbor(x, y, z - 1, cid);
		this.notifyNeighbor(x, y, z + 1, cid);
	}
	
	public void notifyNeighbor(int x, int y, int z, int cid) {
		if(!editingBlocks) {
			int id = this.getBlockIDAt(x, y, z);
			if(Block.blocks[id] instanceof Block) {
				Block.blocks[id].onNeighborBlockChanged(this, x, y, z, cid);
			}
		}
	}
	
	public int getBlockIDAt(int x, int y, int z) {
		return this.getChunk(x >> 4, z >> 4).getBlockID(x & 0xf, y, z & 0xf) & 0xff;
	}
	
	public int getBlockMetaAt(int x, int y, int z) {
		return this.getChunk(x >> 4, z >> 4).getBlockMetadata(x & 0xf, y, z & 0xf) & 0xf;
	}
	
	public void sendBlockPlace(int x, int y, int z, int id, int meta) {
		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.posX = x;
		pk.posY = (byte) y;
		pk.posZ = z;
		pk.id = (byte) id;
		pk.metadata = (byte) meta;
		
		blockUpdates.add(Utils.packBlockPos(x & 0xff, y & 0xff, z & 0xff));
	}
	
	public boolean setBlock(int x, int y, int z, int id, int meta, int flags) {
		
		if(y < 0 || y > 127) return false;
		
		Chunk c = this.getChunk(x >> 4, z >> 4);
		boolean s = c.setBlock(x &0xf, y, z & 0xf, (byte) id, (byte)meta);
		if(s) {
			if((flags & 1) != 0) { //update neighbors
				this.notifyNearby(x, y, z, id);
			}
			
			if((flags & 0x2) != 0) { //update using level listeners
				this.sendBlockPlace(x, y, z, c.getBlockID(x & 0xf, y, z & 0xf), meta); //TODO check
			}
		}
		return s;
		
	}
	
	public void placeBlockMetaNoUpdate(int x, int y, int z, byte meta) {
		Chunk c = this.getChunk(x / 16, z / 16);
		c.setBlockMetadataRaw(x & 0xf, y, z & 0xf, meta);
	}
	
	public void placeBlockAndNotifyNearby(int x, int y, int z, byte id, byte meta) {
		this.setBlock(x, y, z, id, meta, 3);
	}
	
	public void placeBlockMetaAndNotifyNearby(int x, int y, int z, byte meta) {
		Chunk c = this.getChunk(x / 16, z / 16);
		c.setBlockMetadataRaw(x & 0xf, y, z & 0xf, meta);
		this.notifyNearby(x, y, z, c.getBlockID(x & 0xf, y, z & 0xf));
		this.sendBlockPlace(x, y, z, c.getBlockID(x & 0xf, y, z & 0xf), meta);
	}
	
	public Chunk getChunk(int x, int z) {
		if(x <= 15 && x >= 0 && z <= 15 && z >= 0) return this.chunks[x][z];
		return Chunk.emptyChunk;
	}
	
	public void placeBlockAndNotifyNearby(int x, int y, int z, byte id) {
		this.setBlock(x, y, z, id, (byte)0, 3);
	}
	
	public void placeBlock(int x, int y, int z, byte id) {
		if(x < 256 && y < 128 && z < 256 && y >= 0 && x >= 0 && z >= 0) {
			Chunk c = this.chunks[x >> 4][z >> 4];
			c.setBlock(x & 0xf, y, z & 0xf, id, (byte) 0);
			
			this.sendBlockPlace(x, y, z, id, (byte) 0);
		}
	}
	
	public boolean isBlockSolid(int x, int y, int z) {
		Block b = Block.blocks[this.getBlockIDAt(x, y, z)];
		return (b instanceof Block && b.isSolidRender());
	}
	
	public void placeBlock(int x, int y, int z, byte id, byte meta) {
		if(x < 256 && y < 128 && z < 256 && y >= 0 && x >= 0 && z >= 0) {
			Chunk c = this.chunks[x >> 4][z >> 4];
			c.setBlock(x & 0xf, y, z & 0xf, id, meta);
			
			
			this.sendBlockPlace(x, y, z, id, meta);
		}
	}
	
	public void broadcastPacket(MinecraftDataPacket pk) {
		for(Player pl : this.players.values()) {
			pl.dataPacket(pk.clone());
		}
	}
	
	public void broadcastPacketFromPlayer(MinecraftDataPacket pk, Player p) {
		for(Player pl : this.players.values()) {
			if(p.eid != pl.eid) {
				pl.dataPacket(pk.clone());
			}
		}
	}
	
	public static int incrementAndGetNextFreeEID() {
		return ++freeEID;
	}

	public Material getMaterial(int x, int y, int z) {
		int id = this.getBlockIDAt(x, y, z);
		return id > 0 ? Block.blocks[id].material : Material.air;
	}

	public int getHeightmap(int x, int z) {
		Chunk c = this.getChunk(x >> 4, z >> 4);
		
		if(c == null) return 0;
		return c.getHeightmap(x & 0xf, z & 0xf);
	}

	public boolean isAirBlock(int x, int y, int z) {
		return this.getBlockIDAt(x, y, z) == 0;
	}

	public int findTopSolidBlock(int x, int z) {
		if(x < 256 && z < 256 && x >= 0 && z >= 0) {
			int k = 127;
			do {}while(this.getMaterial(x, k, z).isSolid && --k > 0);
			while(k > 0) {
				int id = this.getBlockIDAt(x, k, z);
				if(id == 0 || !Block.blocks[id].material.isSolid && !Block.blocks[id].material.isLiquid) --k;
				else return k + 1;
			}
		}
		return -1;
	}
	
	public void tick() {
		
		/*Timer*/
		this.worldTime++;
		if(Server.superSecretSettings) {
			for(Entity e : this.entities.values()) {
				e.tick();
			}
		}
		
		 //Normal Ticking: Water/Lava
		int ticksAmount = scheduledTickTreeSet.size();
		if(ticksAmount > 1000) ticksAmount = 1000;
		for(int i = 0; i < ticksAmount; ++i) {
			TickNextTickData tick = scheduledTickTreeSet.first();
			if(tick.scheduledTime > this.worldTime) {
				break;
			}
			scheduledTickTreeSet.remove(tick);
			scheduledTickSet.remove(tick);
			if(this.hasChunksAt(tick.posX - 8, tick.posY - 8, tick.posY - 8, tick.posX + 8, tick.posY + 8, tick.posY + 8)) {
				int id = this.getBlockIDAt(tick.posX, tick.posY, tick.posZ);
				if(id > 0 && id == tick.blockID) {
				//	Block.blocks[id].tick(this, tick.posX, tick.posY, tick.posZ, random);
				}
			}
		}
		//Random Ticking
		
		for(int chunkX = 0; chunkX < 16; ++chunkX) {
			for(int chunkZ = 0; chunkZ < 16; ++chunkZ) {
				Chunk c = this.chunks[chunkX][chunkZ];
				int l1 = 0;
				do {
					this.randInt1 = this.randInt1 * 3 + this.randInt2;
					int xyz = this.randInt1 >>> 2;
					int x = xyz & 0xf;
					int z = xyz >>> 8 & 0xf;
					int y = xyz >>> 16 & 0x7f;
					int id = c.blockData[x << 11 | z << 7 | y] & 0xff;
					if(Block.shouldTick[id]) {
					//	Block.blocks[id].tick(this, x + (c.posX << 4), y, z + (c.posZ << 4), random);
					}
				}while(++l1 <= 80);
			}
		}
		
		
		//Queued block sends
		for(int xyz : this.blockUpdates) {
			int x = xyz >> 16 & 0xff;
			int y = xyz >> 8 & 0xff;
			int z = xyz & 0xff;
			
			UpdateBlockPacket pk = new UpdateBlockPacket();
			pk.posX = x;
			pk.posY = (byte) y;
			pk.posZ = z;
			pk.id = (byte) this.getBlockIDAt(x, y, z);
			pk.metadata = (byte) this.getBlockMetaAt(x, y, z);
			
			for(Player p : this.players.values()) {
				p.dataPacket(pk.clone());
			}
		}
		
		this.blockUpdates.clear();
		
	}
	
	public boolean canSeeSky(int x, int y, int z) {
		if(x < 256 && y < 128 && z < 256 && y >= 0 && x >= 0 && z >= 0) {
			return y >= this.chunks[x >> 4][z >> 4].heightMap[x & 0xf][z & 0xf];
		}
		return false;
		
	}

	public boolean mayPlace(int blockID, int x, int y, int z, boolean tgl) {
		
		int blockAt = this.getBlockIDAt(x, y, z);
		//TODO aabbs checks
		if(blockAt == Block.waterStill.blockID || blockAt == Block.waterFlowing.blockID || blockAt == Block.lavaStill.blockID || blockAt == Block.lavaFlowing.blockID || blockAt == Block.fire.blockID || blockAt == Block.snowLayer.blockID) {
			return true;
		}
		
		return blockID > 0 && Block.blocks[blockAt] == null && Block.blocks[blockID].mayPlace(this, x, y, z);
	}

	public void lightColumnChanged(int x, int z, int newheight, int oldheight) {
		//used to update rendering<?>, not needed?
	}
	public void updateLight(LightLayer layer, int startX, int oldHeight, int startZ, int endX, int height, int endZ, boolean b) {
		//some dimension checks, not really needed until 0.12(never)
		if(!this.updateLights) return;
		
		++this.lightUpdatesCount; 
		if(this.lightUpdatesCount == 50) { //TODO stopping updates completely is not good
			--this.lightUpdatesCount;
			return;
		}
		
		int avgX = (startX + endX) / 2;
		int avgZ = (startZ + endZ) / 2;
		
		
		if(this.hasChunkAt(avgX >> 4, avgZ >> 4)) {	
			Chunk chunk = this.getChunk(avgX >> 4, avgZ >> 4);
			//never empty, skipping check
			
			if(b) {
				int size = this.lightUpdates.size();
				int lightUpdateCount = 5;
				if(size < lightUpdateCount) lightUpdateCount = size;
				
				for(int i = 0; i < lightUpdateCount; ++i) {
					size = this.lightUpdates.size();
					LightUpdate update = this.lightUpdates.get(size - i - 1);
					if(update.layer == layer && update.expandToContain(startX, oldHeight, startZ, endX, height, endZ)) {
						--this.lightUpdatesCount;
						return;
					}
				}
			}
			
			LightUpdate update = new LightUpdate(layer, startX, oldHeight, startZ, endX, height, endZ);
			this.lightUpdates.add(update);
			if(this.lightUpdates.size() > 1000000) {
				this.lightUpdates.clear(); //bad
				Logger.warn("More than 1000000 light updates, clearing...");
			}
			--this.lightUpdatesCount;
		}else {
			//if(true) throw new RuntimeException("not has?"+avgX+":"+avgZ);
			--this.lightUpdatesCount;
		}
		
		
	}
	
	public int getRawBrightness(int x, int y, int z) {
		return this.getRawBrightness(x, y, z, true);
	}
	public int getRawBrightness(int x, int y, int z, boolean complex) {
		int lightValue;
		
		if(complex) {
			int blockID = this.getBlockIDAt(x, y, z);
			if(blockID == Block.stoneSlab.blockID || blockID == Block.farmland.blockID) { //TODO do not hardcode
				lightValue = this.getRawBrightness(x, y + 1, z, false);
				int xpos = this.getRawBrightness(x + 1, y, z, false);
				int xneg = this.getRawBrightness(x - 1, y, z, false);
				int zpos = this.getRawBrightness(x, y, z + 1, false);
				int zneg = this.getRawBrightness(x, y, z - 1, false);
			
				if(xpos > lightValue) lightValue = xpos;
				if(xneg > lightValue) lightValue = xneg;
				if(zpos > lightValue) lightValue = zpos;
				if(zneg > lightValue) lightValue = zneg;
				return lightValue;
			}
		}
		if(y < 0) {
			return 0;
		}else if(y > 127) { 
			//TODO also checks some variable(field_2C, m_skydarken in mcped), ignoring for now
			//v12 = 15 - this.skyDarken
			//if(v12 < 0) return 0 else return v12;
			return 15;
		}else {
			Chunk c = this.getChunk(x >> 4, z >> 4);
			return c.getRawBrightness(x & 0xf, y, z & 0xf, /*this.skyDarken*/ 0); //TODO this.skyDarken
		}
		
		
		
		
	}
	public boolean hasChunkAt(int x, int z) {
		return x >= 0 && x < 16 && z >= 0 && z < 16; //TODO do not hardcode
	}

	public void updateLight(LightLayer layer, int startX, int oldHeight, int startZ, int x2, int height, int z2) {
		this.updateLight(layer, startX, oldHeight, startZ, x2, height, z2, true);
	}

	public boolean updateLights() {
		if(this.lightDepth > 49) return false;
		
		++this.lightDepth;
		int maxUpdates = 500;
		
		Logger.info(String.format("Light updates total: %d", this.lightUpdates.size()));
		
		
		while(this.lightUpdates.size() > 0) {
			if(--maxUpdates <= 0) {
				--this.lightDepth;
				return true;
			}
			
			LightUpdate update = this.lightUpdates.get(this.lightUpdates.size() - 1);
			//Logger.info(String.format("updating light: %s", update));
			this.lightUpdates.remove(this.lightUpdates.size() - 1);
			update.update(this);
		}
		
		--this.lightDepth;
		return false;
		
	}

	public int getBrightness(LightLayer layer, int x, int y, int z) {
		if(y < 0 || y > 127) return layer.defaultValue;
		Chunk c = this.getChunk(x >> 4, z >> 4);
		if(c == null) return 0;
		
		return c.getBrightness(layer, x & 0xf, y, z & 0xf);
	}

	public boolean isSkyLit(int x, int y, int z) {
		if(y < 0) return false;
		if(y > 127) return true;
		
		Chunk c = this.getChunk(x >> 4, z >> 4);
		if(c == null) return false;
		return c.isSkyLit(x & 0xf, y, z & 0xf);
	}

	public void setBrightness(LightLayer layer, int x, int y, int z, int brightness) {
		if(y >= 0 && y <= 127) {
			Chunk c = this.getChunk(x >> 4, z >> 4);
			if(c == null) return;
			
			c.setBrightness(layer, x & 0xf, y, z & 0xf, brightness & 0xf);
			//also notifies LevelListeners, but they seem to not do anything
		}
	}
	
	
	
	public void updateLightIfOtherThan(LightLayer layer, int x, int y, int z, int lightLevel) {
		//skipping checks related to dimension(!hasSky probably)
		
		if(layer == LightLayer.SKY) {
			if(this.isSkyLit(x, y, z)) lightLevel = 15;
		}else if(layer == LightLayer.BLOCK) {
			int blockID = this.getBlockIDAt(x, y, z);
			int emission = Block.lightEmission[blockID];
			if(emission > lightLevel) lightLevel = emission;
		}
		
		if(this.getBrightness(layer, x, y, z) != lightLevel) {
			this.updateLight(layer, x, y, z, x, y, z);
		}
		
	}
	
}
