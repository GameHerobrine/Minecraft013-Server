package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;

public class StoneBlock extends Block{

	public StoneBlock(int id) {
		super(id, Material.stone);
	}
	
	@Override
	public int getResource(int meta, BedrockRandom random) {
		return Block.cobblestone.blockID;
	}
}
