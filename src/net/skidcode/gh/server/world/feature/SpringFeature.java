package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class SpringFeature extends Feature{
	public byte blockID;
	public SpringFeature(int blockID) {
		this.blockID = (byte) blockID;
	}
	
	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
        if(world.getBlockIDAt(x, y + 1, z) != Block.stone.blockID)
        {
            return false;
        }
        if(world.getBlockIDAt(x, y - 1, z) != Block.stone.blockID)
        {
            return false;
        }
        if(world.getBlockIDAt(x, y, z) != 0 && world.getBlockIDAt(x, y, z) != Block.stone.blockID)
        {
            return false;
        }
        int l = 0;
        if(world.getBlockIDAt(x - 1, y, z) == Block.stone.blockID)
        {
            l++;
        }
        if(world.getBlockIDAt(x + 1, y, z) == Block.stone.blockID)
        {
            l++;
        }
        if(world.getBlockIDAt(x, y, z - 1) == Block.stone.blockID)
        {
            l++;
        }
        if(world.getBlockIDAt(x, y, z + 1) == Block.stone.blockID)
        {
            l++;
        }
        int i1 = 0;
        if(world.isAirBlock(x - 1, y, z))
        {
            i1++;
        }
        if(world.isAirBlock(x + 1, y, z))
        {
            i1++;
        }
        if(world.isAirBlock(x, y, z - 1))
        {
            i1++;
        }
        if(world.isAirBlock(x, y, z + 1))
        {
            i1++;
        }
        if(l == 3 && i1 == 1)
        {
            world.placeBlock(x, y, z, this.blockID);
            //world.scheduledUpdatesAreImmediate = true; TODO random tick updates
            //Block.blocksList[liquidBlockId].updateTick(world, x, y, z, rand);
            //world.scheduledUpdatesAreImmediate = false;
        }
        return true;
	}

}
