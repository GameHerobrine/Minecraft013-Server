package net.skidcode.gh.server.world.biome.impl;

import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.biome.Biome;
import net.skidcode.gh.server.world.feature.Feature;
import net.skidcode.gh.server.world.feature.PineFeature;
import net.skidcode.gh.server.world.feature.SpruceFeature;
import net.skidcode.gh.server.world.feature.TreeFeature;

public class TaigaBiome extends Biome{
	public Feature getTreeFeature(BedrockRandom r) {
		if(r.nextInt(3) == 0) {
			return new PineFeature();
		}else {
			return new SpruceFeature();
		}
	}
}
