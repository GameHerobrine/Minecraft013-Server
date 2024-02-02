package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.world.World;

public class SnowLayerBlock extends Block{ //TODO some baseclass for non-solid?

	public SnowLayerBlock(int id) {
		super(id, Material.topSnow);
		this.isSolid = false;
		Block.shouldTick[id] = true;
	}
	
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {
		if(world.getBlockIDAt(x, y-1, z) == 0) {
			world.setBlock(x, y, z, (byte)0, (byte)0, 3);
		}
	}
}
