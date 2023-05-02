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
			for(int z = 0; z < 16; ++z) {
				Chunk c = new Chunk(x, z);
				NormalWorldGenerator.generateChunk(w, c);
				w.chunks[x][z] = c;
			}
			Logger.info("Generating "+x+": [0-15] chunks");
		}
		
		w.locationTable = new int[32][32]; //TODO comp with vanilla
	}
	
	protected static float[] getHeights(float[] noises, int chunkX, int chunkY, int chunkZ, int scaleX, int scaleY, int scaleZ) {
		
		if(noises == null) noises = new float[scaleX * scaleZ * scaleY];
		
		return noises;
	}
	
	public static void generateChunk(World w, Chunk c) {
		PerlinNoise pn = new PerlinNoise(w.random, 4);
		float[] n = pn.getRegion(null, 0, 0, 0, 16, 128, 16, 1.121f, 1.121f, 0.5f);
		int i = 0;
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				for(int y = 64; y >= 0; --y) {
					float v = n[i++];
					if(v > 0.75) {
						c.blockData[x][z][y] = (byte) (y == 64 ? Block.grass.blockID : Block.cobblestone.blockID);
					}else {
						c.blockData[x][z][y] = 0;
					}
				}
			}
		}
	}
	
}
