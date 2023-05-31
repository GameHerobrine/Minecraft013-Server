package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.PlantBlock;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.world.World;

public class ReedsBlock extends PlantBlock{

	public ReedsBlock(int id) {
		super(id, Material.plant);
	}
	
	public boolean canSurvive(World world, int x, int y, int z) {
		
		int dY = y - 1;
		int idDown = world.getBlockIDAt(x, dY, z);
		if(idDown == this.blockID) return true;
		else if(idDown != Block.grass.blockID && idDown != Block.dirt.blockID) return false;
		return world.getMaterial(x - 1, dY, z) == Material.water || world.getMaterial(x + 1, dY, z) == Material.water || world.getMaterial(x, dY, z - 1) == Material.water || world.getMaterial(x, dY, z + 1) == Material.water;
	}

}
