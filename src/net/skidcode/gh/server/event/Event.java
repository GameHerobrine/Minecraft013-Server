package net.skidcode.gh.server.event;

import net.skidcode.gh.server.utils.Logger;

//TODO Cancellable
public abstract class Event {
	private static long nextID = 0;
	public boolean isReuseable = true;
	public boolean isCancelled = false;
	public Event() {
		this.isReuseable = false;
	}
	
	public abstract long getID();
	
	public static long getNextFreeID() {
		return Event.nextID++;
	}
	
	@Override
	public boolean equals(Object e) {
		return e instanceof Event && ((Event)e).getID() == this.getID();
	}
}
