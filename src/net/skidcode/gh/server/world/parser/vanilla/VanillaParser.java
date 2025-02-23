package net.skidcode.gh.server.world.parser.vanilla;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.format.PlayerData;

public class VanillaParser {
	
	public static World parseVanillaWorld() throws IOException {
		LevelDatParser levelDat = new LevelDatParser("world/");
		ChunkDataParser chunkDat = new ChunkDataParser("world/");
		World w = new World(0xdeadbeef);
		levelDat.parse(w);
		chunkDat.parse(w);
		for(int x = 0; x < 256; ++x) {
			for(int z = 0; z < 256; ++z) {
				for(int y = 0; y < 128; ++y) {
					int id = w.getBlockIDAt(x, y, z);
					if(id > 0) {
						Block.blocks[id].onPlace(w, x, y, z);
					}
				}
			}
		}
		return w;
	}

	public static void saveVanillaWorld() throws IOException {
		Path p = Paths.get("world/level.dat");
		if(!Files.exists(p)) {
			Files.createFile(p);
		}
		LevelDatParser levelDat = new LevelDatParser(p);
		p = Paths.get("world/chunks.dat");
		if(!Files.exists(p)) {
			Files.createFile(p);
		}
		ChunkDataParser chunkDat = new ChunkDataParser(p);
		
		levelDat.save(Server.world);
		chunkDat.save(Server.world);
		Files.write(Paths.get("world/level.dat"), levelDat.buffer);
		
		Files.write(Paths.get("world/chunks.dat"), chunkDat.buffer);
	}
	
}
