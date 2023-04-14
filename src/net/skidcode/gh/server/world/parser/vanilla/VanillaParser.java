package net.skidcode.gh.server.world.parser.vanilla;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.skidcode.gh.server.Server;
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

	public static void saveVanillaWorld() throws IOException {
		LevelDatParser levelDat = new LevelDatParser("world/"); //TODO custom world folders..?
		ChunkDataParser chunkDat = new ChunkDataParser("world/"); //TODO custom world folders..?
		
		levelDat.save(Server.world);
		chunkDat.save(Server.world);
		Files.write(Paths.get("world/level.dat"), levelDat.buffer);
		
		Files.write(Paths.get("world/chunks.dat"), chunkDat.buffer);
	}
	
}
