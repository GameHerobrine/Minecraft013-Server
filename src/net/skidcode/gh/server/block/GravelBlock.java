package net.skidcode.gh.server.block;

import net.skidcode.gh.server.utils.random.BedrockRandom;

public class GravelBlock extends SandBlock{

	public GravelBlock(int id) {
		super(id);
	}

	@Override
	public int getResource(int meta, BedrockRandom random) {
		return this.blockID;
	}
}
