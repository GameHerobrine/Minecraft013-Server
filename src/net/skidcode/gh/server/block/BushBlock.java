package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class BushBlock extends Block{

	public BushBlock(int id) {
		super(id, Material.plant);
		this.setTicking(true);
		this.setShape(0.5f - 0.2f, 0.0f, 0.5f - 0.2f, 0.2f + 0.5f, 0.2f*3.0f, 0.2f+0.5f);
	}
	//TODO more methods
	public boolean isSolidRender() {
		return false;
	}
	//TODO isCubeShaped -> return false (is it even needed?)
	//TODO getAABB -> return null;
	
	public boolean mayPlaceOn(int id) {
		return id == Block.grass.blockID || id == Block.dirt.blockID || id == Block.farmland.blockID;
	}
	
	public boolean mayPlace(World world, int x, int y, int z) {
		return this.mayPlaceOn(world.getBlockIDAt(x, y - 1, z));
	}
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {
		super.onNeighborBlockChanged(world, x, y, z, meta);
		this.checkAlive(world, x, y, z);
		
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		this.checkAlive(world, x, y, z);
	}
	
	public void checkAlive(World world, int x, int y, int z) {
		if(!this.canSurvive(world, x, y, z)) world.setBlock(x, y, z, (byte)0, (byte)0, 3);
	}
	
	public boolean canSurvive(World world, int x, int y, int z) {
		if(/*world.getRawBrightness(x, y, z) > 7 || */world.canSeeSky(x, y, z)) { //TODO light
			int below = world.getBlockIDAt(x, y - 1, z);
			if(this.mayPlaceOn(below)) return true;	
		}
		
		return false;
	}
	
}
