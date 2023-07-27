package net.skidcode.gh.server.utils.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public abstract class ConfigFile {
	
	public String path2File;
	public HashMap<String, String> data = new HashMap<>();
	public ConfigFile(String path2file) throws IOException {
		this.path2File = path2file;
		if(Files.exists(Paths.get(path2file))) {
			this.read();
		}else {
			this.write();
		}
	}
	public ConfigFile(String path2file, String[][] def) throws IOException {
		this.path2File = path2file;
		if(Files.exists(Paths.get(path2file))) {
			this.read();
		}
		for(String[] s : def) {
			this.data.putIfAbsent(s[0], s[1]);
		}
		this.write();
	}
	public abstract void read() throws IOException;
	public abstract void write() throws IOException;
	
	public abstract String getString(String key, String def);
	public abstract boolean getBoolean(String key, boolean def);
	public abstract int getInteger(String key, int def);
	
}
