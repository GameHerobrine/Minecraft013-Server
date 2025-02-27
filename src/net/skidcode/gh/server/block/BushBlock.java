package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.AABB;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class BushBlock extends Block{

	public BushBlock(int id) {
		super(id, Material.plant);
		this.setTicking(true);
		this.setShape(0.5f - 0.2f, 0.0f, 0.5f - 0.2f, 0.2f + 0.5f, 0.2f*3.0f, 0.2f+0.5f);
	}
	
	public boolean isSolidRender() {
		return false;
	}
	
	@Override
	public AABB getAABB(World world, int x, int y, int z) {
		return null;
	}
	
	public boolean mayPlaceOn(int id) {
		return id == Block.grass.blockID || id == Block.dirt.blockID || id == Block.farmland.blockID;
	}
	
	@Override
	public boolean mayPlace(World world, int x, int y, int z) {
		return this.mayPlaceOn(world.getBlockIDAt(x, y - 1, z));
	}
	
	@Override
	public void neighborChanged(World world, int x, int y, int z, int meta) {
		super.neighborChanged(world, x, y, z, meta);
		this.checkAlive(world, x, y, z);
		
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		this.checkAlive(world, x, y, z);
	}
	
	public void checkAlive(World world, int x, int y, int z) {
		if(!this.canSurvive(world, x, y, z)) world.setBlock(x, y, z, (byte)0, (byte)0, 3);
	}
	
	@Override
	public boolean canSurvive(World world, int x, int y, int z) {
		if(world.getRawBrightness(x, y, z) > 7 || world.canSeeSky(x, y, z)) {
			int below = world.getBlockIDAt(x, y - 1, z);
			if(this.mayPlaceOn(below)) return true;	
		}
		
		return false;
	}
	
}
