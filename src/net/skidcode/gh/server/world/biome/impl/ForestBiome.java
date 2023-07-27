package net.skidcode.gh.server.world.biome.impl;

import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.biome.Biome;
import net.skidcode.gh.server.world.feature.BirchFeature;
import net.skidcode.gh.server.world.feature.Feature;
import net.skidcode.gh.server.world.feature.TreeFeature;

public class ForestBiome extends Biome{
	public Feature getTreeFeature(BedrockRandom r) {
		if(r.nextInt(5) == 0) {
			return new BirchFeature();
		}else {
			r.nextInt();
			return new TreeFeature();
		}
	}
}
