package net.skidcode.gh.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.console.ThreadConsole;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.ConsoleIssuer;
import net.skidcode.gh.server.network.RakNetHandler;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.config.PropertiesFile;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.format.PlayerData;
import net.skidcode.gh.server.world.generator.FlatWorldGenerator;
import net.skidcode.gh.server.world.parser.vanilla.VanillaParser;

public final class Server {
	
	public static boolean running = true;
	public static RakNetHandler handler;
	public static World world;
	private static HashMap<String, Player> id2Player = new HashMap<>();
	public static PropertiesFile properties;
	
	private static int port = 19132;
	public static boolean saveWorld = true;
	public static boolean savePlayerData = true;
	public static long nextTick = 0;
	public static int tps = 0;
	public static int stableTPS = 0;
	public static long lastSecondRecorded = 0;
	
	public static void main(String[] args) throws IOException {
		Logger.info("Starting Server...");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if(Server.saveWorld) {
					Logger.info("Saving world...");
					try {
						VanillaParser.saveVanillaWorld();
						Logger.info("Done saving");
					} catch (IOException e) {
						e.printStackTrace();
					}
					Server.running = false;
				}
			}
		});
		Block.init();
		Logger.info("Creating directories...");
		Files.createDirectories(Paths.get("world/players"));
		Files.createDirectories(Paths.get("world"));
		Logger.info("Loading properties...");
		properties = new PropertiesFile("server.properties", new String[][] {
			{"server-port", "19132"},
			{"save-world", "true"},
			{"save-player-data", "true"},
			{"generate-world", "false"},
			{"world-generator", "DEFAULT"},
		});
		
		try {
			Server.port = Integer.parseInt(properties.data.get("server-port"));
		}catch(Exception e) {
			e.printStackTrace();
			Logger.warn("Failed to get port from properties. Running on 19132.");
		}
		
		try {
			Server.saveWorld = Boolean.parseBoolean(properties.data.get("save-world"));
		}catch(Exception e) {
			e.printStackTrace();
			Logger.warn("Failed to get save-world from properties. World WILL be saved.");
		}
		try { //TODO make something with those try{}catch, theres too much of them
			Server.savePlayerData = Boolean.parseBoolean(properties.data.get("save-player-data"));
		}catch(Exception e) {
			e.printStackTrace();
			Logger.warn("Failed to get save-player-data from properties. Player Data WILL be saved.");
		}
		
		handler = new RakNetHandler();
		
		
		if(Files.exists(Paths.get("world/level.dat"))){
			Logger.info("Loading world...");
			Server.world = VanillaParser.parseVanillaWorld();
			Logger.info("Done!");
		}else if(Boolean.parseBoolean(properties.data.get("generate-world"))) {
			Logger.info("Generating flat world...");
			Server.world = new World();
			FlatWorldGenerator.generateChunks(Server.world);
			Server.world.setSaveSpawn(127, 127);
		}else {
			Logger.error("No world is found.");
			System.exit(0);
		}
		
		
		
		run();
		System.exit(0);
	}
	
	public static Collection<Player> getPlayers(){
		return id2Player.values();
	}
	
	public static Player getPlayer(String id) {
		
		return id2Player.getOrDefault(id, null);
	}
	
	public static void broadcastMessage(String message) {
		for(Player p : Server.getPlayers()) {
			p.sendMessage(message);
		}
	}
	
	public static void removePlayer(String id) {
		Player p = id2Player.remove(id);
		if(p != null) {
			p.onPlayerExit();
			p.world.removePlayer(p.eid);
			p.world = null;
			Logger.info(id+" closed a session.");
		}
		//else Logger.info(id+" closed session which doesnt exist?!");
		
	}
	public static void addPlayer(String id, Player player) {
		Logger.info(id+" has started a new session. Client ID: "+player.clientID+", EID: "+player.eid);
		id2Player.put(id, player);
	}
	
	public static void run(){
		ThreadConsole tc = new ThreadConsole();
		tc.start();
		while(Server.running) {
			long tickTime = System.currentTimeMillis();
			if(Server.nextTick - tickTime <= 0) {
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
				++Server.tps;
				if(tickTime - lastSecondRecorded >= 1000) { //maybe make it better?
					lastSecondRecorded = tickTime;
					Server.stableTPS = Server.tps;
					Server.tps = 0;
				}
				if(Server.nextTick - tickTime < -1000) Server.nextTick = tickTime;
				else Server.nextTick += 50;
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

	public static int getPort() {
		return port;
	}
	
}
