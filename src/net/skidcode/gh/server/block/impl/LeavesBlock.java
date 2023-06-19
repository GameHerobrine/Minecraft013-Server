package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class LeavesBlock extends SolidBlock{

	public LeavesBlock(int id) {
		super(id, Material.leaves);
		this.name = "Leaves";
		this.isSolid = false; //um well... it depends on graphics level...?
		Block.shouldTick[id] = true;
	}
	
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		
	}
}
