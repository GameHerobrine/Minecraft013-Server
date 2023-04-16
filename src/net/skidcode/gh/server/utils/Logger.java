package net.skidcode.gh.server.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Logger {
	
	public static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");  
	
	public static void notice(Object msg) {
		System.out.println("["+timeFormat.format(LocalDateTime.now())+"][NOTICE] "+msg);
	}
	
	public static void raw(Object msg) {
		System.out.println(msg);
	}
	
	public static void error(Object msg) {
		System.out.println("["+timeFormat.format(LocalDateTime.now())+"][ERROR] "+msg);
	}
	
	public static void critical(Object msg) {
		System.err.println("["+timeFormat.format(LocalDateTime.now())+"][CRITICAL] "+msg);
		System.exit(-1);
	}
	
	public static void info(Object... data) {
		System.out.println("["+timeFormat.format(LocalDateTime.now())+"][INFO] "+Arrays.toString(data));
	}
	
	public static void info(Object msg) {
		System.out.println("["+timeFormat.format(LocalDateTime.now())+"][INFO] "+msg);
	}

	public static void warn(Object msg) {
		System.out.println("["+timeFormat.format(LocalDateTime.now())+"][WARNING] "+msg);
	}
	
	public static void cmd(Object msg) {
		System.out.println("["+timeFormat.format(LocalDateTime.now())+"][CMD] "+msg);
	}
}
