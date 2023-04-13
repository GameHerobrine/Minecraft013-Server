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
	
	public void placeBlock(int x, int y, int z, byte id, Player p) { //TODO how?
		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.posX = x;
		pk.posY = (byte) y;
		pk.posZ = z;
		pk.id = (byte) id;
		pk.unknown5 = 0; //meta?
		this.broadcastPacketFromPlayer(pk, p);
		PlaceBlockPacket pkk = new PlaceBlockPacket();
		pkk.posX = x;
		pkk.posY = (byte) y;
		pkk.posZ = z;
		pkk.id = (byte) id;
		pkk.unknown5 = 0;
		this.broadcastPacketFromPlayer(pkk, p);
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
