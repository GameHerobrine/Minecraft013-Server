package net.skidcode.gh.server.world.parser.vanilla;

import java.io.IOException;

import net.skidcode.gh.server.world.World;

public class VanillaParser {
	
	public static World parseVanillaWorld() throws IOException {
		LevelDatParser levelDat = new LevelDatParser("world/"); //TODO custom world folders..?
		ChunkDataParser chunkDat = new ChunkDataParser("world/"); //TODO custom world folders..?
		World w = new World();
		levelDat.parse(w);
		chunkDat.parse(w);
		
		return w;
	}
	
}
