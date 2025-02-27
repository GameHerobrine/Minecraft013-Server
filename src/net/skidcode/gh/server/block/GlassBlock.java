package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;

public class GlassBlock extends HalfTransparentBlock{

	public GlassBlock(int id, Material m, boolean f6c) {
		super(id, m, f6c);
	}

	@Override
	public int getResourceCount(BedrockRandom random) {
		return 0;
	}
	
}
