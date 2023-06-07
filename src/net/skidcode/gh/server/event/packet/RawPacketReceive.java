package net.skidcode.gh.server.event.packet;

import net.skidcode.gh.server.event.Event;

public class RawPacketReceive extends Event{
	private static final long _id = Event.getNextFreeID();
	public String address;
	public int port;
	public byte[] payload;
	public RawPacketReceive(String address, int port, byte[] payload) {
		this.address = address;
		this.port = port;
		this.payload = payload;
	}
	
	@Override
	public long getID() {
		return _id;
	}

}
