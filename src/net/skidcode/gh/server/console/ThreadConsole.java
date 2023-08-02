package net.skidcode.gh.server.console;

import java.util.Scanner;

import net.skidcode.gh.server.Server;

public class ThreadConsole extends Thread{
	public String msg = null;
	public void run() {
		synchronized(this) {
			Scanner sc = new Scanner(System.in);
			while(Server.running) {
				try {
					this.msg = sc.nextLine();
					this.wait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
