package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class LeafBlock extends TransparentBlock{
	
	public LeafBlock(int id) {
		super(id, Material.leaves, false);
		this.unkField_70 = 0;
	}
	
	//TODO more methods
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		//TODO tick
	}
}
