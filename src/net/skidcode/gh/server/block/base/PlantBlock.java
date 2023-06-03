package net.skidcode.gh.server.block.base;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.world.World;

public class PlantBlock extends Block{

	public PlantBlock(int id, Material m) {
		super(id, m);
		this.isSolid = false;
	}
	
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {
		
		if(!world.isBlockSolid(x, y - 1, z)) { //TODO check not just solidness, but ids
			this.onBlockRemoved(world, x, y, z);
		}
	}
	public boolean canSurvive(World world, int x, int y, int z) {
		boolean res = world.canSeeSky(x, y, z);
		if(/*Level::getRawBrightness(a2, a3, a4, a5) > 7 TODO light ||*/ res) {
			int id = world.getBlockIDAt(x, y - 1, z);
			return id == Block.grass.blockID || id == Block.dirt.blockID || id == Block.farmland.blockID;
		}
		return res;
	}
	
}
