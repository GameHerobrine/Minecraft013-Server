package net.skidcode.gh.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.console.ThreadConsole;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.ConsoleIssuer;
import net.skidcode.gh.server.event.EventRegistry;
import net.skidcode.gh.server.event.player.PlayerSendChatMessage;
import net.skidcode.gh.server.event.server.ServerInitialized;
import net.skidcode.gh.server.network.RakNetHandler;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.plugin.Plugin;
import net.skidcode.gh.server.plugin.PluginInfo;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.Utils;
import net.skidcode.gh.server.utils.config.PropertiesFile;
import net.skidcode.gh.server.world.LightLayer;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.generator.FlatWorldGenerator;
import net.skidcode.gh.server.world.generator.NormalWorldGenerator;
import net.skidcode.gh.server.world.parser.vanilla.VanillaParser;

public final class Server {
	public static final int PLUGIN_API_VERSION = 2;
	public static int autoSaveIntervalSeconds = 60;
	public static boolean orderChunksOnServerSide = false;
	public static boolean enableEntitySpawning = false;
	public static boolean enableEntityTicking = false;
	public static boolean enableFireSpread = true;
	public static boolean enableTNTEntity = false;
	public static volatile boolean running = true;
	public static RakNetHandler handler;
	public static World world;
	public static HashMap<PluginInfo, Plugin> plugins = new HashMap<>();
	public static HashMap<String, Player> id2Player = new HashMap<>();
	public static PropertiesFile properties;
	public static final File pluginsPath = new File("plugins/");
	public static volatile int port = 19132;
	public static volatile boolean saveWorld = true;
	public static volatile boolean sendFullChunks = true;
	public static volatile boolean savePlayerData = true;
	public static volatile boolean allowFromDifferentPort = true;
	public static volatile int maxMTUSize = 1000;
	public static volatile boolean enableColors = true;
	public static long nextTick = 0;
	public static int tps = 0;
	public static volatile String serverName = "MCCPP;Demo;Minecraft 0.1.3 Server";
	public static int stableTPS = 0;
	public static long lastSecondRecorded = 0;
	
	
	public static void stop() {
		running = false;
		handler.notifyShutdown();
	}
	
	private static int freeEID = 1;
	public static int incrementAndGetNextFreeEID() {
		return ++freeEID;
	}
	
	public static void main(String[] args) throws IOException {
		Logger.info("Starting Server...");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if(Server.saveWorld && Server.world != null) {
					Logger.info("Saving world...");
					try {
						long l = System.nanoTime();
						VanillaParser.saveVanillaWorld();
						l = System.nanoTime() - l;
						Logger.info("Done saving (Took "+l+"ns)");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				for(Plugin p : Server.plugins.values()) {
					p.onDisable();
				}
				Server.running = false;
			}
		});
		Logger.info("Creating directories...");
		Files.createDirectories(Paths.get("world/players"));
		Files.createDirectories(Paths.get("world"));
		pluginsPath.mkdirs();
		Logger.info("Loading properties...");
		properties = new PropertiesFile("server.properties", new String[][] {
			{"server-port", String.valueOf(Server.port)},
			{"save-world", String.valueOf(Server.saveWorld)},
			{"save-player-data", String.valueOf(Server.savePlayerData)},
			{"generate-world", "true"},
			{"world-generator", "NORMAL"},
			{"server-name", Server.serverName},
			{"world-seed", ""},
			{"max-mtu-size", String.valueOf(maxMTUSize)},
			{"allow-from-different-port", String.valueOf(Server.allowFromDifferentPort)},
			{"enable-terminal-colors", String.valueOf(Server.enableColors)},
			{"send-full-chunks", String.valueOf(Server.sendFullChunks)},
			{"enable-entity-ticking", String.valueOf(Server.enableEntityTicking)},
			{"enable-tnt-entity", String.valueOf(Server.enableTNTEntity)},
			{"enable-fire-spread", String.valueOf(Server.enableFireSpread)},
			{"order-chunks-on-serverside", String.valueOf(Server.orderChunksOnServerSide)},
			{"auto-save-interval-seconds", String.valueOf(Server.autoSaveIntervalSeconds)}
		});
		Server.enableColors = properties.getBoolean("enable-terminal-colors", Server.enableColors);
		Server.serverName = properties.getString("server-name", Server.serverName);
		Server.port = properties.getInteger("server-port", Server.port);
		Server.saveWorld = properties.getBoolean("save-world", Server.saveWorld);
		Server.savePlayerData = properties.getBoolean("save-player-data", Server.savePlayerData);
		Server.maxMTUSize = properties.getInteger("max-mtu-size", Server.maxMTUSize);
		Server.allowFromDifferentPort = properties.getBoolean("allow-from-different-port", Server.allowFromDifferentPort);
		Server.sendFullChunks = properties.getBoolean("send-full-chunks", (Server.sendFullChunks));
		Server.enableTNTEntity = properties.getBoolean("enable-tnt-entity", Server.enableTNTEntity);
		Server.enableEntityTicking = properties.getBoolean("enable-entity-ticking", Server.enableEntityTicking);
		Server.enableFireSpread = properties.getBoolean("enable-fire-spread", Server.enableFireSpread);
		Server.orderChunksOnServerSide = properties.getBoolean("order-chunks-on-serverside", Server.orderChunksOnServerSide);
		Server.autoSaveIntervalSeconds = properties.getInteger("auto-save-interval-seconds", Server.autoSaveIntervalSeconds);
		
		Logger.info("Running server on port "+Server.port);
		
		if(Server.autoSaveIntervalSeconds <= 0) {
			Logger.notice("Autosave interval is less or equal to 0: autosaving is disabled");
		}
		
		if(Server.enableTNTEntity && !Server.enableEntityTicking) {
			Logger.warn("TNT Entity is enabled but entity ticking is not enabled!");
		}
		
		if(Server.port != 19132 && !Server.allowFromDifferentPort) {
			Logger.warn("Server port is not default and clients are not allowed to connect from different port. Vanilla users may be not able to connect!");
		}
		if(!Server.sendFullChunks){
			Logger.warn("Server will not send full chunks. Server and clients world may be desynchronized!");
		}
		try {
			ServerClassLoader classLoader = new ServerClassLoader(new URL[] {}, Server.class.getClassLoader());
			for(int i = 0; i < args.length; ++i) {
				if(args[i].equals("--plugins")) {
					for(String s : args[i+1].split(";")) {
						Class<?> cl = classLoader.loadClass(s);
        				Plugin plugin = (Plugin) cl.newInstance();
        				plugin.onEnable();
					}
				}
			}
			Server.loadPlugins(classLoader);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | MalformedURLException
				| NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException e) {
			Logger.info("Failed to load plugins!");
			e.printStackTrace();
			System.exit(0);
		}
		int iWorldSeed;
		String worldSeed = properties.getString("world-seed", "");
		if(worldSeed.equals("")) {
			iWorldSeed = (int) (System.currentTimeMillis() / 1000L);
		}else {
			try {
				iWorldSeed = Integer.parseInt(worldSeed);
			}catch(NumberFormatException e) {
				iWorldSeed = Utils.stringHash(worldSeed);
			}
		}
		handler = new RakNetHandler();
		boolean generateWorld = properties.getBoolean("generate-world", false);
		if(Files.exists(Paths.get("world/level.dat"))){
			Logger.info("Loading world...");
			long l = System.nanoTime();
			Server.world = VanillaParser.parseVanillaWorld();
			l = System.nanoTime() - l;
			Logger.info("Loading done! (Took "+l+"ns)");
			
		}else if(generateWorld) {
			String type = properties.getString("world-generator", "");
			if(type.equalsIgnoreCase("flat")) {
				Logger.info("Generating flat world...");
				Server.world = new World(iWorldSeed);
				FlatWorldGenerator.generateChunks(Server.world);
				Server.world.setSaveSpawn(127, 127);
			}else
			if(type.equalsIgnoreCase("normal")) {
				Logger.info("Generating normal world...");
				Server.world = new World(iWorldSeed);
				try {
					NormalWorldGenerator.generateChunks(Server.world);
				}catch(StackOverflowError e) {throw new RuntimeException();}
				Server.world.setSaveSpawn(127, 127);
			}else {
				Server.saveWorld = false;
				Logger.critical("Type '"+type+"' is not found!");
				Server.stop();
				System.exit(0);
			}
			
		}else {
			Logger.error("No world is found.");
			System.exit(0);
		}
		Logger.info("Done!");
		EventRegistry.handleEvent(new ServerInitialized());
		run();
		System.exit(0);
	}
	
	private static void loadPlugins(ServerClassLoader classLoader) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, ClassNotFoundException, InstantiationException {
		if(classLoader instanceof URLClassLoader) {
			File[] fs = Server.pluginsPath.listFiles();
			for(File f : fs) {
				if(f.getName().endsWith(".jar")) {
					classLoader.addUrl(f.toURI().toURL());
					
				}
			}
			for(File f : fs) {
				if(f.getName().endsWith(".jar")) {
					FileInputStream fileinputstream = new FileInputStream(f);
					ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
					ZipEntry entry = null;
					PluginInfo info = new PluginInfo();
					Plugin plugin = null;
					while((entry  = zipinputstream.getNextEntry()) != null) {
                    	if(entry.getName().equals("plugin.properties")) {
                    		Scanner sc = new Scanner(zipinputstream);
							while(sc.hasNext()) {
								String line = sc.nextLine();
                    			String[] propValue = line.split("=");
                    			String prop = propValue[0].trim();
                    			String value = propValue[1].trim();
                    			if(prop.equals("name")) info.name = value;
                    			else if(prop.equals("api")) info.api = Integer.parseInt(value);
                    			else if(prop.equals("description")) info.description = value;
                    			else if(prop.equals("author")) info.author = value;
                    			else if(prop.equals("version")) info.version = value;
                    			else if(prop.equals("mainclass")) {
                    				Class<?> cl = classLoader.loadClass(value.replace("/", "."));
                    				plugin = (Plugin) cl.newInstance();
                    				plugin.onEnable();
                    			}
                    			
                    		};
                    		
                			if(Server.PLUGIN_API_VERSION != info.api) {
                				Logger.warn(String.format("Plugin "+info.name+" "+info.version+" api version does not match server(%d != %d)!", info.api, Server.PLUGIN_API_VERSION));
                			}
                    		
                    		if(plugin != null) {
                    			Server.plugins.put(info, plugin);
                    			Logger.info("Plugin "+info.name+" "+info.version+" by "+info.author+" was loaded!");
                    		}
                    		
                    	}else if(entry.getName().endsWith(".class")) {
                    		String className = entry.getName().replace("/", ".");
                    		classLoader.loadClass(className.substring(0, className.length() - 6));
                    	}
                    }
					zipinputstream.close();
					fileinputstream.close();
					
				}
			}
		}
		classLoader.close();
		
	}

	public static Collection<Player> getPlayers(){
		return id2Player.values();
	}
	
	public static Player getPlayer(String id) {
		return id2Player.getOrDefault(id, null);
	}
	
	public static void broadcastMessage(String message, boolean console) {
		if(console) Logger.info(message);
		for(Player p : Server.getPlayers()) {
			p.sendMessage(message);
		}
	}
	
	/**
	 * Sends a message to everyone on the server and into console.
	 * @param message
	 */
	public static void broadcastMessage(String message) {
		broadcastMessage(message, true);
	}
	public static void removePlayer(String id) {
		removePlayer(id, "unknown");
	}
	public static void removePlayer(String id, String reason) {
		Player p = id2Player.remove(id);
		if(p != null) {
			p.onPlayerExit();
			if(p.world != null) {
				p.world.removePlayer(p.eid);
				p.world = null;
			}
			Logger.info(id+" closed a session: "+reason);
		}
	}
	
	public static void addPlayer(String id, Player player) {
		Logger.info(id+" has started a new session. Client ID: "+player.clientID+", EID: "+player.eid);
		id2Player.put(id, player);
	}
	
	public static long lastTimeSaved = 0;
	public static void run(){
		ThreadConsole tc = new ThreadConsole();
		tc.start();
		lastTimeSaved = System.nanoTime();
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
				
				Server.world.tick();
				
				if(Server.autoSaveIntervalSeconds > 0) {
					long now = System.nanoTime();
					if(now - lastTimeSaved > (long)Server.autoSaveIntervalSeconds*1000000000) {
						try {
							VanillaParser.saveVanillaWorld();
						} catch (IOException e) {
							Logger.error("Autosave failed!");
							e.printStackTrace();
						}
						lastTimeSaved = System.nanoTime();
					}
				}
				
				while(Server.world.updateLights()); //TODO check
				++Server.tps;
				if(tickTime - lastSecondRecorded >= 1000) { //maybe make it better?
					lastSecondRecorded = tickTime;
					Server.stableTPS = Server.tps;
					Server.tps = 0;
				}
				if(Server.nextTick - tickTime < -1000) Server.nextTick = tickTime;
				else Server.nextTick += 50;
			}else {
				try {
					Thread.sleep(Server.nextTick - tickTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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

	public static void sendMessageBy(Player player, String message) {
		PlayerSendChatMessage ev = new PlayerSendChatMessage(player, message);
		EventRegistry.handleEvent(ev);
		
		if(!ev.isCancelled) {
			message = ev.message;
			Server.broadcastMessage("<"+player.nickname+"> "+message);
		}
	}

	public static void kickPlayer(Player player, String reason) {
		Server.handler.closeSession(player.identifier, reason);
	}
	
}
