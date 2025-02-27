package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;

public class ClayBlock extends Block{

	public ClayBlock(int id) {
		super(id, Material.clay);
	}
	//TODO methods
	
	@Override
	public int getResource(int meta, BedrockRandom random) {
		return 0;
	}
	
	@Override
	public int getResourceCount(BedrockRandom random) {
		return 4;
	}
}
