package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class GrassBlock extends Block{

	public GrassBlock(int id) {
		super(id, Material.dirt);
		this.unkField_4 = 3;
		this.setTicking(true);
		
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		if(world.getRawBrightness(x, y + 1, z) <= 3) {
			Material m = world.getMaterial(x, y + 1, z);
			if(m.blocksLight) {
				if(random.nextInt(4) == 0) {
					world.setBlock(x, y, z, (byte) Block.dirt.blockID, (byte) 0, 2);
				}
			}
		}else if(world.getRawBrightness(x, y + 1, z) > 8) {
			int tryX = x + random.nextInt(3) - 1;
			int tryY = y + random.nextInt(5) - 3;
			int tryZ = z + random.nextInt(3) - 1;
			
			if(world.getBlockIDAt(tryX, tryY, tryZ) == Block.dirt.blockID) {
				if(world.getRawBrightness(tryX, tryY + 1, tryZ) > 3) {
					Material m = world.getMaterial(tryX, tryY + 1, tryZ);
					if(!m.blocksLight) world.setBlock(tryX, tryY, tryZ, (byte)Block.grass.blockID, (byte)0, 2);
				}
			}
			
		}
	}
}
