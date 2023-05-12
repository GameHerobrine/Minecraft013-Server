package net.skidcode.gh.server.world.generator;

import java.util.stream.IntStream;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.noise.PerlinNoise;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.chunk.Chunk;
//TODO rename to LevelSource + make non-static
public class NormalWorldGenerator {
	
	public static void generateChunks(World w) {
		for(int x = 0; x < 16; ++x) {
			Logger.info("Generating "+x+": [0-15] chunks");
			for(int z = 0; z < 16; ++z) {
				w.chunks[x][z] = w.levelSource.getChunk(x, z);
			}
		}
		
		w.locationTable = new int[32][32]; //TODO comp with vanilla
	}
	
	protected static float[] getHeights(float[] noises, int chunkX, int chunkY, int chunkZ, int scaleX, int scaleY, int scaleZ) {
		if(noises == null) noises = new float[scaleX * scaleZ * scaleY];
		
		return noises;
	}
	
}
