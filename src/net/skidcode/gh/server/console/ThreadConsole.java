package net.skidcode.gh.server.console;

import java.util.Scanner;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.utils.Logger;

public class ThreadConsole extends Thread{
	public String msg = null;
	public void run() {
		synchronized(this) {
			Scanner sc = new Scanner(System.in);
			while(Server.running) {
				this.msg = sc.nextLine();
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
