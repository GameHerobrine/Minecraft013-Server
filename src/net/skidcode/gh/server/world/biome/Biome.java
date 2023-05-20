package net.skidcode.gh.server.world.biome;

import java.util.Random;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.world.biome.impl.FlatBiome;
import net.skidcode.gh.server.world.biome.impl.ForestBiome;
import net.skidcode.gh.server.world.biome.impl.TaigaBiome;
import net.skidcode.gh.server.world.feature.TreeFeature;

public class Biome {
	public static Biome rainForest;
	public static Biome swampland;
	public static Biome seasonalForest;
	public static Biome forest;
	public static Biome savanna;
	public static Biome shrubland;
	public static Biome taiga;
	public static Biome desert;
	public static Biome plains;
	public static Biome iceDesert;
	public static Biome tundra;
	public static Biome[] biomes = new Biome[4096];
	
	public String name;
	public byte topBlock = (byte) Block.grass.blockID;
	public byte fillerBlock = (byte) Block.dirt.blockID; //TODO maybe stone??
	
	public TreeFeature getTreeFeature(Random r) {
		return new TreeFeature();
	}
	
	public static Biome __getBiome(float temp, float rain) {
		rain *= temp;
		
		if (temp < 0.1f) {
			return Biome.tundra;
		}
		
		if (rain < 0.2f) {
			if (temp < 0.5f) {
				return Biome.tundra;
			}
			if (temp < 0.95f) {
				return Biome.savanna;
			}else {
				return Biome.desert;
			}
		}
		
		if (rain > 0.5f && temp < 0.7f) {
			return Biome.swampland;
		}
		if (temp < 0.5f) {
			return Biome.taiga;
		}
		if (temp < 0.97f) {
			if (rain < 0.35f) {
				return Biome.shrubland;
			}else {
				return Biome.forest;
			}
		}
		if (rain < 0.45f) {
			return Biome.plains;
		}
		if (rain < 0.9f) {
			return Biome.seasonalForest;
		}
		return Biome.rainForest;

	}
	
	
	public static void recalc() {
		for(int i = 0; i < 64; ++i) {
			for(int j = 0; j < 64; ++j) {
				Biome.biomes[i + (j * 64)] = Biome.__getBiome((float) i / 63f, (float) j / 63f);
			}
		}
		Biome.desert.topBlock = Biome.desert.fillerBlock = (byte) Block.sand.blockID;
		Biome.iceDesert.topBlock = Biome.iceDesert.fillerBlock = (byte) Block.sand.blockID;
	}
	
	static {
		Biome.rainForest = new Biome(); //while biomes in 0.1 have colors, it is useless for server
		Biome.rainForest.name = "Rainforest";
		Biome.swampland = new Biome();
		Biome.swampland.name = "Swampland";
		Biome.seasonalForest = new Biome();
		Biome.seasonalForest.name = "Seasonal Forest";
		Biome.forest = new ForestBiome();
		Biome.forest.name = "Forest";
		Biome.savanna = new FlatBiome();
		Biome.savanna.name = "Savanna";
		Biome.shrubland = new Biome();
		Biome.shrubland.name = "Shrubland";
		Biome.taiga = new TaigaBiome();
		Biome.taiga.name = "Taiga";
		//Biome::setSnowCovered(v18); taiga, does nothing in 0.1.3
		Biome.desert = new FlatBiome();
		Biome.desert.name = "Desert";
		Biome.plains = new FlatBiome();
		Biome.plains.name = "Plains";
		Biome.iceDesert = new FlatBiome(); //Isnt used in 0.1.3
		Biome.tundra = new FlatBiome();
		Biome.tundra.name = "Tundra"; //Biome::setSnowCovered(v30);
		
		Biome.recalc();
	}

	public static Biome getBiome(float blockTemperature, float blockRainfall) {
		int i = (int) (blockTemperature * 63f);
		int j = (int) (blockRainfall * 63f);
		return Biome.biomes[i + j * 64];
	}
}
