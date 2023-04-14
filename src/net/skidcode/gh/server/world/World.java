package net.skidcode.gh.server.world;

import java.util.HashMap;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.protocol.PlaceBlockPacket;
import net.skidcode.gh.server.network.protocol.RemoveEntityPacket;
import net.skidcode.gh.server.network.protocol.UpdateBlockPacket;
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
	
	public void placeBlock(int x, int y, int z, byte id) { //TODO how? + rem unk5
		
		this.chunks[x >> 4][z >> 4].blockData[x & 0xf][z & 0xf][y] = id;
		this.chunks[x >> 4][z >> 4].blockMetadata[x & 0xf][z & 0xf][y] = 0;

		/*PlaceBlockPacket pkk = new PlaceBlockPacket(); Looks like not neccessary here, even though vanilla sends it too
		pkk.posX = x;
		pkk.posY = (byte) y;
		pkk.posZ = z;
		pkk.id = (byte) id;
		pkk.unknown5 = unk5;
		pkk.unknown1 = p.eid;
		this.broadcastPacketFromPlayer(pkk, p);*/
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
