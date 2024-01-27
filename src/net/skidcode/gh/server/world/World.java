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
	
	public int randInt1, randInt2;
	
	public TreeSet<TickNextTickData> scheduledTickTreeSet;
	public HashSet<TickNextTickData> scheduledTickSet;
	public World(int seed) {
		this.worldSeed = seed;
		this.random = new BedrockRandom(seed);
		this.biomeSource = new BiomeSource(this);
		this.levelSource = new RandomLevelSource(this, seed); //TODO API
		this.scheduledTickTreeSet = new TreeSet<>();
		this.scheduledTickSet = new HashSet<>();
		this.randInt1 = 0x283AE83; //it is static in 0.1
		this.randInt2 = 0x3C6EF35F;
	}
	
	public void addEntity(Entity entity) {
		entity.world = this;
		this.entities.put(entity.eid, entity);
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
	
	public boolean hasChunksAt(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if(minY > -1 && minY < 128) {
			for(int chunkX = minX >> 4; chunkX <= maxX >> 4; ++chunkX) {
				for(int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; ++chunkZ) {
					if(chunkX < 0 || chunkZ < 0 || chunkX > 15 || chunkZ > 15 || this.chunks[chunkX][chunkZ] == null) return false;
				}
			}
			return true;
		}
		return false;
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
		return this.getChunk(x >> 4, z >> 4).getBlockMetadata(x & 0xf, y, z & 0xf) & 0xff;
	}
	
	public void sendBlockPlace(int x, int y, int z, int id, int meta) {
		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.posX = x;
		pk.posY = (byte) y;
		pk.posZ = z;
		pk.id = (byte) id;
		pk.metadata = (byte) meta;
		
		for(Player p : this.players.values()) {
			p.dataPacket(pk);
		}
	}
	
	public void placeBlockAndNotifyNearby(int x, int y, int z, byte id, byte meta) {
		if(x < 256 && y < 128 && z < 256 && y >= 0 && x >= 0 && z >= 0) {
			Chunk c = this.chunks[x >> 4][z >> 4];
			c.setBlock(x & 0xf, y, z & 0xf, id, meta);
			
			if(id > 0) Block.blocks[id].onBlockAdded(this, x, y, z);
			this.notifyNearby(x, y, z, id);
			
			this.sendBlockPlace(x, y, z, id, meta);
		}
	}
	
	public void placeBlockMetaAndNotifyNearby(int x, int y, int z, byte meta) {
		Chunk c = this.getChunk(x / 16, z / 16);
		c.setBlockMetadata(x & 0xf, y, z & 0xf, meta);
		this.notifyNearby(x, y, z, c.getBlockID(x & 0xf, y, z & 0xf));
		this.sendBlockPlace(x, y, z, c.getBlockID(x & 0xf, y, z & 0xf), meta);
	}
	
	public Chunk getChunk(int x, int z) {
		if(x <= 15 && x >= 0 && z <= 15 && z >= 0) return this.chunks[x][z];
		return Chunk.emptyChunk;
	}
	
	public void placeBlockAndNotifyNearby(int x, int y, int z, byte id) {
		if(x < 256 && y < 128 && z < 256 && y >= 0 && x >= 0 && z >= 0) {
			Chunk c = this.chunks[x >> 4][z >> 4];
			c.setBlock(x & 0xf, y, z & 0xf, id, (byte) 0);
			
			if(id > 0) Block.blocks[id].onBlockAdded(this, x, y, z);
			
			this.notifyNearby(x, y, z, id);
			this.sendBlockPlace(x, y, z, id, (byte)0);
		}
	}
	
	public void placeBlock(int x, int y, int z, byte id) {
		if(x < 256 && y < 128 && z < 256 && y >= 0 && x >= 0 && z >= 0) {
			Chunk c = this.chunks[x >> 4][z >> 4];
			c.setBlock(x & 0xf, y, z & 0xf, id, (byte) 0);
			
			if(id > 0) Block.blocks[id].onBlockAdded(this, x, y, z);
			
			this.sendBlockPlace(x, y, z, id, (byte) 0);
		}
	}
	
	public boolean isBlockSolid(int x, int y, int z) {
		Block b = Block.blocks[this.getBlockIDAt(x, y, z)];
		return (b instanceof Block && b.material.isSolid);
	}
	
	public void placeBlock(int x, int y, int z, byte id, byte meta) {
		if(x < 256 && y < 128 && z < 256 && y >= 0 && x >= 0 && z >= 0) {
			Chunk c = this.chunks[x >> 4][z >> 4];
			c.setBlock(x & 0xf, y, z & 0xf, id, meta);
			
			if(id > 0) Block.blocks[id].onBlockAdded(this, x, y, z);
			
			this.sendBlockPlace(x, y, z, id, meta);
		}
	}
	
	public void broadcastPacket(MinecraftDataPacket pk) {
		for(Player pl : this.players.values()) {
			pl.dataPacket(pk);
		}
	}
	
	public void broadcastPacketFromPlayer(MinecraftDataPacket pk, Player p) {
		for(Player pl : this.players.values()) {
			if(p.eid != pl.eid) {
				pl.dataPacket(pk);
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

	public int getHeightValue(int x, int z) {
		if(x < 256 && z < 256 && x >= 0 && z >= 0) {
			return this.chunks[x >> 4][z >> 4].heightMap[x & 0xf][z & 0xf];
		}
		return 0;
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
					Block.blocks[id].tick(this, tick.posX, tick.posY, tick.posZ, random);
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
						Block.blocks[id].tick(this, x + (c.posX << 4), y, z + (c.posZ << 4), random);
					}
				}while(++l1 <= 80);
			}
		}
		
	}
	
	public boolean canSeeSky(int x, int y, int z) {
		if(x < 256 && y < 128 && z < 256 && y >= 0 && x >= 0 && z >= 0) {
			return y >= this.chunks[x >> 4][z >> 4].heightMap[x & 0xf][z & 0xf];
		}
		return false;
		
	}
	
}
