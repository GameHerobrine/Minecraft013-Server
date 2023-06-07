package net.skidcode.gh.server.event.packet;

import net.skidcode.gh.server.event.Event;
import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.player.Player;

public class DataPacketSend extends Event{
	public static final DataPacketSend INSTANCE = new DataPacketSend();
	private static final long _id = Event.getNextFreeID();
	
	public DataPacketSend(Player p, MinecraftDataPacket pk) {
		this.player = p;
		this.packet = pk;
	}
	
	public DataPacketSend() {
		super();
	}
	
	public Player player;
	public MinecraftDataPacket packet;

	@Override
	public long getID() {
		return _id;
	}
}
