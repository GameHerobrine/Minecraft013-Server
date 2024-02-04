package net.skidcode.gh.server.world.generator;

import net.skidcode.gh.server.world.biome.Biome;
import net.skidcode.gh.server.world.chunk.Chunk;

public interface LevelSource {
	
	public Chunk getChunk(int chunkX, int chunkZ);
	public void prepareHeights(int chunkX, int chunkZ, byte[] blockIDS, Biome[] biomes, float[] temperatures);
	public float[] getHeights(float[] heights, int chunkX, int chunkY, int chunkZ, int scaleX, int scaleY, int scaleZ);
	public void buildSurfaces(int chunkX, int chunkZ, byte[] blockIDS, Biome[] biomes);
	public void postProcess(int chunkX, int chunkZ);
}
