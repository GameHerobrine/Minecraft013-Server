package net.skidcode.gh.server.world.generator;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.world.LightLayer;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.parser.vanilla.ChunkDataParser;

public class NormalWorldGenerator {
	
	public static void generateChunks(World w) {
		for(int x = 0; x < 16; ++x) {
			Logger.info("Generating "+x+": [0-15] chunks");
			for(int z = 0; z < 16; ++z) {
				w.chunks[x][z] = w.levelSource.getChunk(x, z);
				while(w.updateLights());
			}
		}
		for(int x = 0; x < 16; ++x) {
			Logger.info("Populating "+x+": [0-15] chunks");
			for(int z = 0; z < 16; ++z) {
				w.levelSource.postProcess(x, z);
			}
		}
		
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				w.chunks[x][z].clearUpdateMap();
			}
		}
		
		
		w.locationTable = ChunkDataParser.locTable;
	}
	
	protected static float[] getHeights(float[] noises, int chunkX, int chunkY, int chunkZ, int scaleX, int scaleY, int scaleZ) {
		if(noises == null) noises = new float[scaleX * scaleZ * scaleY];
		
		return noises;
	}
	
}
