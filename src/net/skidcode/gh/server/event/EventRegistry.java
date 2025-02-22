package net.skidcode.gh.server.event;

import java.lang.reflect.Field;
import java.util.ArrayList;
import sun.misc.Unsafe;

public class EventRegistry {
	public static ArrayList<EventListener<? extends Event>>[] array = new ArrayList[4];
	
	public static final Unsafe safe;
	static {
		Field f;
		try {
			f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			safe = (Unsafe) f.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void registerListener(Class<? extends Event> class1, EventListener<? extends Event> listener) {
		Event e;
		try {
			e = (Event) safe.allocateInstance(class1);
		} catch (InstantiationException ee) {
			throw new RuntimeException(ee);
		}
		
		int id = (int) e.getID();
		if(array.length <= id) {
			ArrayList<EventListener<? extends Event>>[] n = new ArrayList[id+1];
			System.arraycopy(array, 0, n, 0, array.length);
			array = n;
		}
		if(array[id] == null) array[id] = new ArrayList<EventListener<? extends Event>>();
		array[id].add(listener);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void handleEvent(Event event) {
		ArrayList<EventListener<? extends Event>> arr;
		if(array.length > event.getID()) {
			 arr = array[(int)event.getID()];
		}else {
			return;
		}
		EventListener el;
		int index = 0;
		if(arr != null) {
			do {
				el = arr.get(index++);
				el.handleEvent(event);
			}while(!event.isCancelled && index < arr.size());
		}
	}
}
