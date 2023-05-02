package net.skidcode.gh.server.event;

import net.skidcode.gh.server.utils.Logger;

//TODO Cancellable
public abstract class Event {
	public String name = this.getClass().getSimpleName();
	public boolean isReuseable = true;
	
	public Event() {
		this.isReuseable = false;
	}
	
	@Override
	public boolean equals(Object e) {
		return e instanceof Event && ((Event)e).name.equals(this.name);
	}
}
