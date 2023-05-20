package net.skidcode.gh.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import net.skidcode.gh.server.event.packet.DataPacketReceive;
import net.skidcode.gh.server.network.RakNetHandler;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.plugin.Plugin;
import net.skidcode.gh.server.plugin.PluginInfo;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.Utils;
import net.skidcode.gh.server.utils.config.PropertiesFile;
import net.skidcode.gh.server.utils.noise.PerlinNoise;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.utils.random.MTRandom;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.generator.FlatWorldGenerator;
import net.skidcode.gh.server.world.generator.NormalWorldGenerator;
import net.skidcode.gh.server.world.parser.vanilla.VanillaParser;

public final class Server {
	
	public static volatile boolean running = true;
	public static RakNetHandler handler;
	public static World world;
	private static HashMap<String, Plugin> plugins = new HashMap<>();
	private static HashMap<String, Player> id2Player = new HashMap<>();
	public static PropertiesFile properties;
	public static final File pluginsPath = new File("plugins/");
	private static volatile int port = 19132;
	public static volatile boolean saveWorld = true;
	public static volatile boolean savePlayerData = true;
	public static long nextTick = 0;
	public static int tps = 0;
	public static volatile String serverName = "MCCPP;Demo;Minecraft 0.1.3 Server";
	public static int stableTPS = 0;
	public static long lastSecondRecorded = 0;
	
	public static void stop() {
		running = false;
		handler.notifyShutdown();
	}
	
	public static void main(String[] args) throws IOException {
		if(true) {
			World w = new World(113318802);
			w.levelSource.getChunk(158 >> 4, 162 >> 4);
		}
		Logger.info("Starting Server...");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if(Server.saveWorld && Server.world != null) {
					Logger.info("Saving world...");
					try {
						VanillaParser.saveVanillaWorld();
						Logger.info("Done saving");
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
		Block.init();
		Logger.info("Creating directories...");
		Files.createDirectories(Paths.get("world/players"));
		Files.createDirectories(Paths.get("world"));
		pluginsPath.mkdirs();
		Logger.info("Loading properties...");
		properties = new PropertiesFile("server.properties", new String[][] {
			{"server-port", "19132"},
			{"save-world", "true"},
			{"save-player-data", "true"},
			{"generate-world", "false"},
			{"world-generator", "NORMAL"},
			{"server-name", "MCCPP;Demo;Minecraft 0.1.3 Server"}
		});
		Server.serverName = properties.data.get("server-name");
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
		
		try {
			Server.loadPlugins();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | MalformedURLException
				| NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException e) {
			Logger.info("Failed to load plugins!");
			e.printStackTrace();
			System.exit(0);
		}
		
		handler = new RakNetHandler();
		
		if(Files.exists(Paths.get("world/level.dat"))){
			Logger.info("Loading world...");
			Server.world = VanillaParser.parseVanillaWorld();
		}else if(properties.getNullsafe("generate-world").equals("true")) {
			String type = properties.getNullsafe("world-generator");
			if(type.equalsIgnoreCase("flat")) {
				Logger.info("Generating flat world...");
				Server.world = new World(0xfe1ebeef);
				FlatWorldGenerator.generateChunks(Server.world);
				Server.world.setSaveSpawn(127, 127);
			}else
			if(type.equalsIgnoreCase("normal")) {
				Logger.info("Generating normal world...");
				Server.world = new World(Utils.stringHash("nyan"));
				NormalWorldGenerator.generateChunks(Server.world);
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
		
		
		run();
		System.exit(0);
	}
	
	private static void loadPlugins() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, ClassNotFoundException, InstantiationException {
		ServerClassLoader classLoader = new ServerClassLoader(new URL[] {}, Server.class.getClassLoader());
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
                    		Scanner sc = new Scanner((InputStream)classLoader.getResource(entry.getName()).getContent());
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
                    		if(plugin != null) {
                    			Server.plugins.put(info.name, plugin);
                    			Logger.info("Plugin "+info.name+" "+info.version+" by "+info.author+" was loaded!");
                    		}
                    		
                    		sc.close();
                    		break;
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
	
}
