package net.skidcode.gh.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import net.skidcode.gh.server.console.ThreadConsole;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.ConsoleIssuer;
import net.skidcode.gh.server.network.RakNetHandler;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.parser.vanilla.VanillaParser;

public final class Server {
	
	public static boolean running = true;
	public static final RakNetHandler handler = new RakNetHandler();
	public static World world;
	private static HashMap<String, Player> id2Player = new HashMap<>();
	
	
	
	public static void main(String[] args) throws IOException {
		Server.world = VanillaParser.parseVanillaWorld();
		run();
	}
	
	public static Collection<Player> getPlayers(){
		return id2Player.values();
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
		Logger.info(id+" has started a new session. Client ID: "+player.clientID+", EID: "+player.eid);
		id2Player.put(id, player);
	}
	
	public static void run(){
		ThreadConsole tc = new ThreadConsole();
		tc.start();
		while(Server.running) {
			Server.handler.process();
			if(tc.msg != null) {
				try {
					String[] cmdsplt = tc.msg.split(" ");
					if(cmdsplt.length > 0) {
						CommandBase cmd = CommandBase.commands.get(cmdsplt[0]);
						
						if(cmd == null) {
							ConsoleIssuer.INSTANCE.sendOutput("Unknown command.");
						}else {
							String out = cmd.processCommand(ConsoleIssuer.INSTANCE, Arrays.copyOfRange(cmdsplt, 1, cmdsplt.length));
							if(out.length() > 0) ConsoleIssuer.INSTANCE.sendOutput(out);
						}
					}
					
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					synchronized(tc) {
						tc.msg = null;
						tc.notify();
					}
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static Player getPlayerByNickname(String nickname) {
		for(Player p : getPlayers()) {
			if(p.nickname.equals(nickname)) return p;
		}
		return null;
	}
	public static Player getPlayerByNickname(String nickname, boolean ignoreCase) {
		for(Player p : getPlayers()) {
			if(ignoreCase ? p.nickname.equalsIgnoreCase(nickname) : p.nickname.equals(nickname)) return p;
		}
		return null;
	}
	
}
