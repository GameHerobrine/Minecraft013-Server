package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class ReedsFeature extends Feature{

	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		for(int l = 0; l < 20; ++l) {
			int blockX = (int) ((x + (Integer.toUnsignedLong(rand.nextInt()) & 3)) - (Integer.toUnsignedLong(rand.nextInt()) & 3));
			int blockZ = (int) ((z + (Integer.toUnsignedLong(rand.nextInt()) & 3)) - (Integer.toUnsignedLong(rand.nextInt()) & 3));
			if (world.isAirBlock(blockX, y, blockZ) && (world.getMaterial(blockX - 1, y - 1, blockZ) == Material.water || world.getMaterial(blockX + 1, y - 1, blockZ) == Material.water || world.getMaterial(blockX, y - 1, blockZ - 1) == Material.water || world.getMaterial(blockX, y - 1, blockZ + 1) == Material.water)) {
				int nextInt3 = 2 + rand.nextInt(rand.nextInt(3) + 1);
				for (int i2 = 0; i2 < nextInt3; i2++) {
					if (Block.reeds.canSurvive(world, blockX, y + i2, blockZ)) { //TODO a bit non-vanilla, make vanilla
						world.placeBlock(blockX, y + i2, blockZ, (byte) Block.reeds.blockID);
					}
				 }
			}
		}
		return true;
		
	}

}
