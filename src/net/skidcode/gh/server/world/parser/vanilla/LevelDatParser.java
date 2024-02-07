package net.skidcode.gh.server.world.parser.vanilla;

import java.io.IOException;
import java.nio.file.Path;

import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.data.WorldDataFile;

public class LevelDatParser extends WorldDataFile{
	
	public int seed;
	public int spawnX, spawnY, spawnZ;
	public int worldTime;
	public int saveTime;
	public String worldName;
	
	public LevelDatParser(Path p) throws IOException {
		super(p, 0);
	}

	public LevelDatParser(String string) throws IOException {
		super(string+"/level.dat", 0);
	}

	@Override
	public void parse(World world) {
		
		this.getInt();
		this.getInt();
		
		world.worldSeed = this.getInt(); //should be long, but 0.1 write<long> writes int
		world.spawnX = this.getInt();
		world.spawnY = this.getInt();
		world.spawnZ = this.getInt();
		
		world.unknown5 = this.getInt(); //somethin idk
		
		world.worldTime = ((long)this.getInt() & 0xffffffffl);
		world.saveTime = this.getInt();
		world.name = this.getString();
		
	}
	
	@Override
	public void save(World world) {
		this.putInt(0x01000000); //dat version
		this.putInt(8*4+2+world.name.length()); //data size
		
		this.putInt(world.worldSeed);
		this.putInt(world.spawnX);
		this.putInt(world.spawnY);
		this.putInt(world.spawnZ);
		this.putInt(world.unknown5);
		this.putInt((int)world.worldTime); 
		this.putInt((int) (System.currentTimeMillis() / 1000L));
		this.putString(world.name);
	}
	
}
