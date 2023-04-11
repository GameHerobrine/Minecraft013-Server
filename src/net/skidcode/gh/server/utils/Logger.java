package net.skidcode.gh.server.utils;

public class Logger {
	public static void notice(Object msg) {
		System.out.println("[NOTICE] "+msg);
	}
	
	public static void error(Object msg) {
		System.out.println("[ERROR] "+msg);
	}

	public static void info(Object msg) {
		System.out.println("[INFO] "+msg);
	}

	public static void warn(Object msg) {
		System.out.println("[WARNING] "+msg);
	}
}
