package net.skidcode.gh.server.world.biome;

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
	public static Biome[] biomes;
	
	
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
            }
            return Biome.desert;
        } else if (rain > 0.5f && temp < 0.7f) {
            return Biome.swampland;
        } else {
            if (temp < 0.5f) {
                return Biome.taiga;
            }
            if (temp < 0.97f) {
                if (rain < 0.35f) {
                    return Biome.shrubland;
                }
                return Biome.forest;
            } else if (rain < 0.45f) {
                return Biome.plains;
            } else {
                if (rain < 0.9f) {
                    return Biome.seasonalForest;
                }
                return Biome.rainForest;
            }
        }

	}
	
	public static void recalc() {
		for(int i = 0; i < 64; ++i) {
			for(int j = 0; j < 64; ++j) {
				Biome.biomes[i + (j * 64)] = Biome.__getBiome(i / 63, j / 63);
			}
		}
		/*
		v8 = Biome::desert;
		  v9 = *(Tile::sand + 8);
		  *(Biome::desert + 33) = v9;
		  *(v8 + 32) = v9;
		  v10 = *(Tile::sand + 8);
		  v11 = Biome::iceDesert;
		  *(Biome::iceDesert + 33) = v10;
		  *(v11 + 32) = v10;*/
	}
	
	
}
