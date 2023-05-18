package net.skidcode.gh.server.world.generator;

import java.util.Random;

import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.noise.PerlinNoise;
import net.skidcode.gh.server.utils.random.MTRandom;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.biome.Biome;

public class BiomeSource {
	public PerlinNoise temperatureNoise;
	public PerlinNoise rainfallNoise;
	public PerlinNoise detailNoise;
	
	public float[] temperatureNoises;
	public float[] rainfallNoises;
	public float[] detailNoises;
	
	public BiomeSource(World w) {
		this.temperatureNoise = new PerlinNoise(new MTRandom(w.worldSeed * 9871), 4);
        this.rainfallNoise = new PerlinNoise(new MTRandom(w.worldSeed * 39811), 4);
        this.detailNoise = new PerlinNoise(new MTRandom(w.worldSeed * 543321), 2);
	}
	
	public Biome getBiome(int x, int z) {
        return getBiomeBlock(x, z, 1, 1)[0];
    }

	public Biome[] getBiomeBlock(int x, int z, int xSize, int zSize) {
		
		this.temperatureNoises = this.temperatureNoise.getRegion(null, x, z, xSize, zSize, 0.025f, 0.025f, 0.25f);
		this.rainfallNoises = this.rainfallNoise.getRegion(null, x, z, xSize, zSize, 0.05f, 0.05f, 0.3333f);
		this.detailNoises = this.detailNoise.getRegion(null, x, z, xSize, zSize, 0.25f, 0.25f, 0.588f);
		
		Biome[] localBiomeArray = new Biome[xSize * zSize];
		
		int index = 0;
		
		for(int blockX = 0; blockX < xSize; ++blockX) {
			for(int blockZ = 0; blockZ < zSize; ++blockZ) {
				float blockRainfall = (((this.detailNoises[index] * 1.1F) + 0.5F) * 0.002F) + (((this.rainfallNoises[index] * 0.15F) + 0.5F) * 0.998F);
				float f1 = 1 - ((((this.detailNoises[index] * 1.1f) + 0.5f) * 0.01f) + (((this.temperatureNoises[index] * 0.15f) + 0.7f) * 0.99f));
				float blockTemperature = 1 - (f1 * f1);

				if(blockTemperature < 0) {
					blockTemperature = 0;
					
				}else if(blockTemperature > 1) {
					blockTemperature = 1;
				}
				
				if(blockRainfall < 0) {
					blockRainfall = 0;
				}else if(blockRainfall > 1) {
					blockRainfall = 1;
				}
				
				this.temperatureNoises[index] = blockTemperature;
				this.rainfallNoises[index] = blockRainfall;
				localBiomeArray[index++] = Biome.getBiome(blockTemperature, blockRainfall);
			}
		}
		
		return localBiomeArray;
	}
	
	public float[] getTemperatureBlock(int x, int z, int xSize, int zSize) {
		this.temperatureNoises = this.temperatureNoise.getRegion(null, x, z, xSize, zSize, 0.025f, 0.025f, 0.25f);
		this.detailNoises = this.detailNoise.getRegion(null, x, z, xSize, zSize, 0.25f, 0.25f, 0.588f);
		
		int index = 0;

		for(int blockX = 0; blockX < xSize; ++blockX) {
			for(int blockZ = 0; blockZ < zSize; ++blockZ) {
				float f = 1.0f - ((((this.detailNoises[index] * 1.1f) + 0.5f) * 0.01f) + (((this.temperatureNoises[index] * 0.15f) + 0.7f) * 0.99f));
				float f1 = 1.0f - (f*f);
				if(f1 < 0) f1 = 0;
				else if(f1 > 0) f1 = 1;
				
				this.temperatureNoises[index++] = f1;
			}
		}
		
		return this.temperatureNoises;
	}
}
