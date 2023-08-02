package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class CactusFeature extends Feature{

	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		for(int l = 0; l < 10; l++)
        {
            int i1 = (x + rand.nextInt(8)) - rand.nextInt(8);
            int j1 = (y + rand.nextInt(4)) - rand.nextInt(4);
            int k1 = (z + rand.nextInt(8)) - rand.nextInt(8);
            if(!world.isAirBlock(i1, j1, k1))
            {
                continue;
            }
            int l1 = 1 + rand.nextInt(rand.nextInt(3) + 1);
            for(int i2 = 0; i2 < l1; i2++)
            {
                if(Block.cactus.canSurvive(world, i1, j1 + i2, k1))
                {
                    world.placeBlock(i1, j1 + i2, k1, (byte) Block.cactus.blockID);
                }
            }

        }

        return true;
	}

}
