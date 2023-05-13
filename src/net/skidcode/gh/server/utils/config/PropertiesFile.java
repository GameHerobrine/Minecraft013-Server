package net.skidcode.gh.server.utils.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class PropertiesFile extends ConfigFile{

	public PropertiesFile(String path2file) throws IOException {
		super(path2file);
	}
	public PropertiesFile(String path2file, String[][] def) throws IOException {
		super(path2file, def);
	}
	@Override
	public void read() throws IOException {
		List<String> strl = Files.readAllLines(Paths.get(this.path2File));
		
		for(String s : strl) {
			String[] sa = s.split("=");
			this.data.put(sa[0], sa.length > 1 ? sa[1] : "");
		}
		
	}
	
	public String getNullsafe(String key) {
		return this.data.getOrDefault(key, "");
	}
	
	@Override
	public void write() throws IOException{
		List<String> strl = new ArrayList<>();
		for(Entry<String, String> entr : this.data.entrySet()) {
			strl.add(entr.getKey()+"="+entr.getValue());
		}
		Files.write(Paths.get(this.path2File), strl);
	}

}
