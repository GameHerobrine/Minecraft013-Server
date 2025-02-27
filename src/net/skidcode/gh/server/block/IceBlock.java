package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.LightLayer;
import net.skidcode.gh.server.world.World;

public class IceBlock extends HalfTransparentBlock{

	public IceBlock(int id) {
		super(id, Material.ice, false);
		this.slipperiness = 0.98f;
		this.setTicking(true);
	}
	
	@Override
	public int getResourceCount(BedrockRandom random) {
		return 0;
	}
	
	@Override
	public void onRemove(World world, int x, int y, int z) {
		Material mat = world.getMaterial(x, y-1, z);
		if(mat.blocksMotion || mat.isLiquid) {
			world.setBlock(x, y, z, Block.waterFlowing.blockID, 0, 3);
		}
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		int brightness = world.getBrightness(LightLayer.BLOCK, x, y, z);
		int blockLight = Block.lightBlock[this.blockID];
		
		if(brightness > 11 - blockLight) {
			//XXX call spawnResources
			world.setBlock(x, y, z, Block.waterStill.blockID, 0, 3);
		}
	}
}
