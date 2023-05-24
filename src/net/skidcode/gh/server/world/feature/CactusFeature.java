package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class CactusFeature extends Feature{

	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		for (int i = 0; i < 10; i++) {
            int xPos = (x + rand.nextInt(8)) - rand.nextInt(8);
            int yPosA = (y + rand.nextInt(4)) - rand.nextInt(4);
            int zPos = (z + rand.nextInt(8)) - rand.nextInt(8);
            if (world.isAirBlock(xPos, yPosA, zPos)) {
                int nextInt4 = 1 + rand.nextInt(rand.nextInt(3) + 1);
                for (int i2 = 0; i2 < nextInt4; i2++) {
                	int yPos = yPosA + i2;
                    if (Block.cactus.canSurvive(world, xPos, yPos, zPos)) {
                        world.placeBlock(xPos, yPos, zPos, (byte) Block.cactus.blockID);
                    }
                }
            }
        }
        return true;
	}

}
