package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.AABB;
import net.skidcode.gh.server.world.World;

public class FarmBlock extends Block{

	public FarmBlock(int id) {
		super(id, Material.dirt);
		this.setTicking(true);
		this.setShape(0.0f, 0.0f, 0.0f, 1.0f, 0.9375f, 1.0f);
		this.setLightBlock(255);
	}
	//TODO methods
	
	@Override
	public AABB getAABB(World world, int x, int y, int z) {
		return this.boundingBox.set(x, y, z, x+1, y+1, z+1);
	}
	
	public boolean isSolidRender() {
		return false;
	}
}
