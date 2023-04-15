package net.skidcode.gh.server.world.parser.vanilla;

import java.io.IOException;

import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.nbt.WorldNBTFile;

public class LevelDatParser extends WorldNBTFile{
	
	public int seed;
	public int spawnX, spawnY, spawnZ;
	public int worldTime;
	public int saveTime;
	public String worldName;
	
	public LevelDatParser(String path2World) throws IOException {
		super(path2World+"/level.dat", 8);
	}

	@Override
	public void parse(World world) {
		world.worldSeed = this.getInt(); //should be long, but 0.1 write<long> writes int
		world.spawnX = this.getInt();
		world.spawnY = this.getInt();
		world.spawnZ = this.getInt();
		
		world.unknown5 = this.getInt(); //somethin idk
		
		world.worldTime = this.getInt();
		world.saveTime = this.getInt();
		world.name = this.getString();
		
	}
	
	@Override
	public void save(World world) {
		this.putInt(world.worldSeed);
		this.putInt(world.spawnX);
		this.putInt(world.spawnY);
		this.putInt(world.spawnZ);
		this.putInt(world.unknown5);
		this.putInt(world.worldTime);
		this.putInt(world.saveTime);
		this.putString(world.name);
	}
	
}
