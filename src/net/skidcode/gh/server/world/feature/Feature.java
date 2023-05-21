package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public abstract class Feature {
	public abstract boolean place(World world, BedrockRandom rand, int x, int y, int z);
}
