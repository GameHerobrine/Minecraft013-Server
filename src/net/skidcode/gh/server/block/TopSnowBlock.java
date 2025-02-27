package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.AABB;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.LightLayer;
import net.skidcode.gh.server.world.World;

public class TopSnowBlock extends Block {

	public TopSnowBlock(int id) {
		super(id, Material.topSnow);
		this.setShape(0, 0, 0, 1, 0.125f, 1);
		this.setTicking(true);
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		int b = world.getBrightness(LightLayer.BLOCK, x, y, z);
		if(b > 11) {
			//spawn resources
			world.setBlock(x, y, z, 0, 0, 3);
		}
	}
	
	@Override
	public void neighborChanged(World world, int x, int y, int z, int meta) {
		this.checkCanSurvive(world, x, y, z);
	}
	
	@Override
	public AABB getAABB(World world, int x, int y, int z) {
		return null;
	}
	
	@Override
	public boolean mayPlace(World world, int x, int y, int z) {
		int id = world.getBlockIDAt(x, y-1, z);
		//null check is not in original
		if(id == 0 || Block.blocks[id] == null || Block.blocks[id].isSolidRender() != true) {
			return false;
		}
		return Block.blocks[id].material.blocksMotion;
	}
	
	public boolean checkCanSurvive(World world, int x, int y, int z) {
		if(this.mayPlace(world, x, y, z)) return true;
		
		//XXX spawn resources
		world.setBlock(x, y, z, 0, 0, 3);
		
		return false;
	}
	
	@Override
	public int getResource(int meta, BedrockRandom random) {
		return 0;
	}
	
	@Override
	public int getResourceCount(BedrockRandom random) {
		return 0;
	}
	
	@Override
	public boolean isSolidRender() {
		return false;
	}
}
