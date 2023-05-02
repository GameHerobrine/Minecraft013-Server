package net.skidcode.gh.server.event;

public abstract class EventListener<T extends Event> {
	
	public EventListener(T event) {
		EventRegistry.registerListener(event.getClass(), this);
	}
	
	public abstract void handleEvent(T event);
}
