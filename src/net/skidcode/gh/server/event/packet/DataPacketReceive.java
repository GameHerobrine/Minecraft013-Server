package net.skidcode.gh.server.event.packet;

import net.skidcode.gh.server.event.Event;
import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.player.Player;

public class DataPacketReceive extends Event{
	
	public static final DataPacketReceive INSTANCE = new DataPacketReceive();
	private static final long _id = Event.getNextFreeID();
	public DataPacketReceive(Player p, MinecraftDataPacket pk) {
		this.player = p;
		this.packet = pk;
	}
	
	public DataPacketReceive() {
		super();
	}
	
	public Player player;
	public MinecraftDataPacket packet;

	@Override
	public long getID() {
		return _id;
	}
}
