package net.skidcode.gh.server.utils;

import java.util.Arrays;

public class Logger {
	public static void notice(Object msg) {
		System.out.println("[NOTICE] "+msg);
	}
	
	public static void raw(Object msg) {
		System.out.println(msg);
	}
	
	public static void error(Object msg) {
		System.out.println("[ERROR] "+msg);
	}
	
	public static void critical(Object msg) {
		System.err.println("[CRITICAL] "+msg);
		System.exit(-1);
	}
	
	public static void info(Object... data) {
		System.out.println("[INFO] "+Arrays.toString(data));
	}
	
	public static void info(Object msg) {
		System.out.println("[INFO] "+msg);
	}

	public static void warn(Object msg) {
		System.out.println("[WARNING] "+msg);
	}
	
	public static void cmd(Object msg) {
		System.out.println("[CMD] "+msg);
	}
}
