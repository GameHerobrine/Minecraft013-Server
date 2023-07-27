package net.skidcode.gh.server.event.server;

import net.skidcode.gh.server.event.Event;

public class ServerInitialized extends Event {
	private static final long _id = Event.getNextFreeID();
	
	@Override
	public long getID() {
		return _id;
	}

}
