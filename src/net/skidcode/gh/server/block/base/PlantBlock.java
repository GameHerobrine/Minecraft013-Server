package net.skidcode.gh.server.block.base;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.world.World;

public class PlantBlock extends Block{

	public PlantBlock(int id, Material m) {
		super(id, m);
	}
	
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {
		
		if(!world.isBlockSolid(x, y - 1, z)) { //TODO check not just solidness, but ids
			this.onBlockRemoved(world, x, y, z);
		}
	}
}
