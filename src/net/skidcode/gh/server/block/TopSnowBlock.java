package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.world.World;

public class TopSnowBlock extends Block {

	public TopSnowBlock(int id) {
		super(id, Material.topSnow);
		this.setShape(0, 0, 0, 1, 0.125f, 1);
		this.setTicking(true);
	}
	
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {
		if(world.getBlockIDAt(x, y-1, z) == 0) {
			world.setBlock(x, y, z, (byte)0, (byte)0, 3);
		}
	}
	
	//TODO methods
	public boolean isSolidRender() {
		return false;
	}
}
