package net.skidcode.gh.server;

import java.util.HashMap;

import net.skidcode.gh.server.network.RakNetHandler;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.raknet.server.RakNetServer;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.world.World;

public final class Server {
	
	public static boolean running = true;
	public static final RakNetHandler handler = new RakNetHandler();
	public static World world;
	private static HashMap<String, Player> id2Player = new HashMap<>();
	
	public static void main(String[] args) {
		Server.world = new World();
		run();
	}
	
	public static Player getPlayer(String id) {
		return id2Player.getOrDefault(id, null);
	}
	public static void removePlayer(String id) {
		Player p = id2Player.remove(id);
		if(p != null) {
			p.world.removePlayer(p.eid);
			
			p.world = null;
			Logger.info(id+" closed a session.");
		}
		else Logger.info(id+" closed session which doesnt exist?!");
		
	}
	public static void addPlayer(String id, Player player) {
		Logger.info(id+" has started a new session. Client ID: "+player.clientID);
		id2Player.put(id, player);
	}
	
	public static void run(){
		while(Server.running) {
			
			Server.handler.process();
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
