package net.skidcode.gh.server.world;

import java.util.HashMap;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.protocol.RemoveEntityPacket;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.chunk.Chunk;

public class World {
	
	public HashMap<Integer, Player> players = new HashMap<>();
	private int freeEID = 1;
	public int worldSeed = 0xd34db33f;
	public Chunk[][] chunks = new Chunk[16][16];
	
	public int spawnX, spawnY, spawnZ;
	public String name;
	public int worldTime, saveTime;
	public int[][] locationTable;
	public int unknown5;
	
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
	
	public void removeBlock(int x, int y, int z) {
		this.chunks[x >> 4][z >> 4].blockData[x & 0xf][z & 0xf][y] = 0;
		this.chunks[x >> 4][z >> 4].blockMetadata[x & 0xf][z & 0xf][y] = 0;
	}
	
	public void placeBlock(int x, int y, int z, byte id) {
		this.chunks[x >> 4][z >> 4].blockData[x & 0xf][z & 0xf][y] = id;
		this.chunks[x >> 4][z >> 4].blockMetadata[x & 0xf][z & 0xf][y] = 0;
	}
	
	public void broadcastPacketFromPlayer(MinecraftDataPacket pk, Player p) {
		for(Player pl : this.players.values()) {
			if(p.eid != pl.eid) {
				pl.dataPacket(pk.clone());
			}
		}
	}
	
	public int incrementAndGetNextFreeEID() {
		return ++freeEID;
	}
	
}
