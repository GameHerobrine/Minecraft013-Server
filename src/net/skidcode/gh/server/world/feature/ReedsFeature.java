package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class ReedsFeature extends Feature{

	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		int nextInt = (x + rand.nextInt(4)) - rand.nextInt(4);
		int nextInt2 = (z + rand.nextInt(4)) - rand.nextInt(4);
		if (world.isAirBlock(nextInt, y, nextInt2) && (world.getMaterial(nextInt - 1, y - 1, nextInt2) == Material.water || world.getMaterial(nextInt + 1, y - 1, nextInt2) == Material.water || world.getMaterial(nextInt, y - 1, nextInt2 - 1) == Material.water || world.getMaterial(nextInt, y - 1, nextInt2 + 1) == Material.water)) {
			int nextInt3 = 2 + rand.nextInt(rand.nextInt(3) + 1);
			for (int i2 = 0; i2 < nextInt3; i2++) {
				if (Block.reeds.canSurvive(world, nextInt, y + i2, nextInt2)) {
					world.placeBlock(nextInt, y + i2, nextInt2, (byte) Block.reeds.blockID);
				}
			 }
		}
		return true;
	}

}
