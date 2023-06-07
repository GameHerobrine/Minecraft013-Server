package net.skidcode.gh.server.event;

public interface EventListener<T extends Event> {
	public abstract void handleEvent(T event);
}
