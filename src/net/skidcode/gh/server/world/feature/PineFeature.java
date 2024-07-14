package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class PineFeature extends Feature{

	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		int l = rand.nextInt(5) + 7;
        int i1 = l - rand.nextInt(2) - 3;
        int j1 = l - i1;
        int k1 = 1 + rand.nextInt(j1 + 1);
        boolean flag = true;
        if(y < 1 || y + l + 1 > 128)
        {
            return false;
        }
        for(int l1 = y; l1 <= y + 1 + l && flag; l1++)
        {
            int j2 = 1;
            if(l1 - y < i1)
            {
                j2 = 0;
            } else
            {
                j2 = k1;
            }
            for(int l2 = x - j2; l2 <= x + j2 && flag; l2++)
            {
                for(int k3 = z - j2; k3 <= z + j2 && flag; k3++)
                {
                    if(l1 >= 0 && l1 < 128)
                    {
                        int j4 = world.getBlockIDAt(l2, l1, k3);
                        if(j4 != 0 && j4 != Block.leaves.blockID)
                        {
                            flag = false;
                        }
                    } else
                    {
                        flag = false;
                    }
                }

            }

        }

        if(!flag)
        {
            return false;
        }
        int i2 = world.getBlockIDAt(x, y - 1, z);
        if(i2 != Block.grass.blockID && i2 != Block.dirt.blockID || y >= 128 - l - 1)
        {
            return false;
        }
        world.placeBlock(x, y - 1, z, (byte)Block.dirt.blockID);
        int k2 = 0;
        for(int i3 = y + l; i3 >= y + i1; i3--)
        {
            for(int l3 = x - k2; l3 <= x + k2; l3++)
            {
                int k4 = l3 - x;
                for(int l4 = z - k2; l4 <= z + k2; l4++)
                {
                    int i5 = l4 - z;
                    if((Math.abs(k4) != k2 || Math.abs(i5) != k2 || k2 <= 0) && !Block.solid[world.getBlockIDAt(l3, i3, l4)])
                    {
                        world.placeBlock(l3, i3, l4, (byte)Block.leaves.blockID, (byte)1);
                    }
                }

            }

            if(k2 >= 1 && i3 == y + i1 + 1)
            {
                k2--;
                continue;
            }
            if(k2 < k1)
            {
                k2++;
            }
        }

        for(int j3 = 0; j3 < l - 1; j3++)
        {
            int i4 = world.getBlockIDAt(x, y + j3, z);
            if(i4 == 0 || i4 == Block.leaves.blockID)
            {
                world.placeBlock(x, y + j3, z, (byte)Block.log.blockID, (byte)1);
            }
        }

        return true;
	}

}
