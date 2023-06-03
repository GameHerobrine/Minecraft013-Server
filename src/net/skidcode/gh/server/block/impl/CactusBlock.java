package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.world.World;
//TODO placement & breaking fixes
public class CactusBlock extends SolidBlock{

	public CactusBlock(int id) {
		super(id, Material.cactus);
		this.name = "Cactus";
		this.isSolid = false;
	}
	
	public boolean canSurvive(World world, int x, int y, int z) {
		/*if(world.getMaterial(x - 1, y, z).isSolid || world.getMaterial(x + 1, y, z).isSolid ||
			world.getMaterial(x, y, z - 1).isSolid || world.getMaterial(x, y, z + 1).isSolid) {
			return false;
		}*/
		if(world.getMaterial(x - 1, y, z).isSolid || world.getMaterial(x + 1, y, z).isSolid || world.getMaterial(x, y, z - 1).isSolid || world.getMaterial(x, y, z + 1).isSolid) return false;
		int id = world.getBlockIDAt(x, y - 1, z);
		return id == Block.cactus.blockID || id == Block.sand.blockID;
	}
	
}
