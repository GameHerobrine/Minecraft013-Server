package net.skidcode.gh.server.block.base;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class LiquidFlowingBlock extends Block{

	public LiquidFlowingBlock(int id, Material m) {
		super(id, m);
	}
	
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		int depth = this.getDepth(world, x, y, z);
		if(depth > 0) {
			//if(this.material == Material.lava && depth < 8) random.nextInt(4); //TODO
		}
	}
	public int getDepth(World world, int x, int y, int z) {
		if(world.getMaterial(x, y, z) != this.material) return -1;
		return world.getBlockMetaAt(x, y, z);
	}
	
}
