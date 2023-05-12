package net.skidcode.gh.server.world;

import java.util.HashMap;
import java.util.Random;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.protocol.RemoveEntityPacket;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.chunk.Chunk;
import net.skidcode.gh.server.world.generator.BiomeSource;
import net.skidcode.gh.server.world.generator.LevelSource;
import net.skidcode.gh.server.world.generator.RandomLevelSource;

public class World {
	
	public HashMap<Integer, Player> players = new HashMap<>();
	private int freeEID = 1;
	public int worldSeed = 0x256512;
	public Random random;
	public Chunk[][] chunks = new Chunk[16][16];
	
	public int spawnX, spawnY, spawnZ;
	public String name = "world";
	public int worldTime = 0, saveTime = 0;
	public int[][] locationTable;
	public int unknown5 = 0;
	public BiomeSource biomeSource;
	public LevelSource levelSource;
	public World(int seed) {
		this.worldSeed = seed;
		this.random = new Random(seed);
		this.biomeSource = new BiomeSource(this);
		this.levelSource = new RandomLevelSource(this, seed); //TODO API
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
		Chunk c = this.chunks[x >> 4][z >> 4];
		int cBX = x & 0xf;
		int cBZ = z & 0xf;
		for(int y = 125; y >= 0; --y) {
			int id = c.blockData[cBX][cBZ][y];
			if(id > 0) {
				Block b = Block.blocks[id];
				if(b.material.isSolid) {
					int idup = c.blockData[cBX][cBZ][y+1];
					int idupper = c.blockData[cBX][cBZ][y+2];
					if(idup == 0 && idupper == 0) { //TODO isSolid & isLiquid checks
						this.spawnY = y+2;
						return;
					}
					
				}
			}
		}
		this.spawnY = 127;
	}
	
	public void removeBlock(int x, int y, int z) {
		this.chunks[x >> 4][z >> 4].blockData[x & 0xf][z & 0xf][y] = 0;
		this.chunks[x >> 4][z >> 4].blockMetadata[x & 0xf][z & 0xf][y] = 0;
		
		this.notifyNeighbor(x - 1, y, z);
		this.notifyNeighbor(x + 1, y, z);
		this.notifyNeighbor(x, y - 1, z);
		this.notifyNeighbor(x, y + 1, z);
		this.notifyNeighbor(x, y, z - 1);
		this.notifyNeighbor(x, y, z + 1);
	}
	
	public void notifyNeighbor(int x, int y, int z) {
		int id = this.getBlockIDAt(x, y, z);
		if(Block.blocks[id] instanceof Block) {
			Block.blocks[id].onNeighborBlockChanged(this, x, y, z, this.getBlockMetaAt(x, y, z));
		}
	}
	
	public int getBlockIDAt(int x, int y, int z) {
		return this.chunks[x >> 4][z >> 4].blockData[x & 0xf][z & 0xf][y];
	}
	
	public int getBlockMetaAt(int x, int y, int z) {
		return this.chunks[x >> 4][z >> 4].blockMetadata[x & 0xf][z & 0xf][y];
	}
	
	public void placeBlock(int x, int y, int z, byte id) {
		this.chunks[x >> 4][z >> 4].blockData[x & 0xf][z & 0xf][y] = id;
		this.chunks[x >> 4][z >> 4].blockMetadata[x & 0xf][z & 0xf][y] = 0;
	}
	
	public boolean isBlockSolid(int x, int y, int z) {
		Block b = Block.blocks[this.getBlockIDAt(x, y, z)];
		return b instanceof Block ? b.material.isSolid : false;
	}
	
	public void placeBlock(int x, int y, int z, byte id, byte meta) {
		this.chunks[x >> 4][z >> 4].blockData[x & 0xf][z & 0xf][y] = id;
		this.chunks[x >> 4][z >> 4].blockMetadata[x & 0xf][z & 0xf][y] = meta;
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
	
	public int incrementAndGetNextFreeEID() {
		return ++freeEID;
	}
	
}
