package net.skidcode.gh.server.world.generator;

import java.util.Random;

import net.skidcode.gh.server.utils.noise.PerlinNoise;
import net.skidcode.gh.server.world.World;

public class BiomeSource {
	public PerlinNoise temperatureNoise;
	public PerlinNoise rainfallNoise;
	public PerlinNoise detailNoise;
	public BiomeSource(World w) {
		this.temperatureNoise = new PerlinNoise(new Random(w.worldSeed * 9871), 4);
        this.rainfallNoise = new PerlinNoise(new Random(w.worldSeed * 39811), 4);
        this.detailNoise = new PerlinNoise(new Random(w.worldSeed * 543321), 2);
	}
	
	public float getTemperatureBlock(int x, int z, int xSize, int zSize) {
		return 1; //TODO method from 0.1.3
	}
}
