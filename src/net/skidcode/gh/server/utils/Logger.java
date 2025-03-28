package net.skidcode.gh.server.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import net.skidcode.gh.server.Server;

public class Logger {
	public static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");  
	
	public static void notice(Object msg) {
		String msgg = "["+timeFormat.format(LocalDateTime.now())+"][NOTICE] "+msg;
		if(Server.enableColors) {
			System.out.println(ANSIColors.CYAN+msgg+ANSIColors.RESET);
		}else {
			System.out.println(msgg);
		}
	}
	
	public static void raw(Object msg) {
		System.out.println(msg);
	}
	
	public static void error(Object msg) {
		String msgg = "["+timeFormat.format(LocalDateTime.now())+"][ERROR] "+msg;
		if(Server.enableColors) {
			System.out.println(ANSIColors.RED+msgg+ANSIColors.RESET);
		}else {
			System.out.println(msg);
		}
		
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
		String msgg = "["+timeFormat.format(LocalDateTime.now())+"][WARNING] "+msg;
		if(Server.enableColors) {
			System.out.println(ANSIColors.ORANGE+msgg+ANSIColors.RESET);
		}else {
			System.out.println(msg);
		}
	}
	
	public static void cmd(Object msg) {
		System.out.println("["+timeFormat.format(LocalDateTime.now())+"][CMD] "+msg);
	}
	
	enum ANSIColors{
		RESET("\u001B[0m"),
		BLACK("\u001B[30m"),
		RED("\u001B[31m"),
		GREEN("\u001B[32m"),
		YELLOW("\u001B[33m"),
		BLUE("\u001B[34m"),
		PURPLE("\u001B[35m"),
		CYAN("\u001B[36m"),
		WHITE("\u001B[37m"),
		
		ORANGE("\u001b[38;5;208m");
		
		public String color;
		@Override
		public String toString() {
			return this.color;
		}
		private ANSIColors(String s) {
			color = s;
		}
	}
}
