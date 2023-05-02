package net.skidcode.gh.server.event;

import java.util.ArrayList;
import java.util.HashMap;

public class EventRegistry {
	public static HashMap<Class<? extends Event>, ArrayList<EventListener<? extends Event>>> listeners = new HashMap<>();
	
	
	public static void registerListener(Class<? extends Event> class1, EventListener<? extends Event> listener) {
		if(listeners.get(class1) == null) listeners.put(class1, new ArrayList<>());
		listeners.get(class1).add(listener);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void handleEvent(Event event) {
		ArrayList<EventListener<? extends Event>> arr = listeners.get(event.getClass());
		if(arr != null) {
			for(EventListener el : arr) {
				el.handleEvent(event);
			}
		}
	}
}
