package net.skidcode.gh.server.block;

import net.skidcode.gh.server.utils.random.BedrockRandom;

public class ObsidianBlock extends StoneBlock{

	public ObsidianBlock(int id) {
		super(id);
	}

	@Override
	public int getResource(int meta, BedrockRandom random) {
		return Block.obsidian.blockID;
	}
	@Override
	public int getResourceCount(BedrockRandom random) {
		return 1;
	}
}
