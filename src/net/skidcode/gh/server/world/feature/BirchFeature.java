package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class BirchFeature extends Feature{

	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		int nextInt = rand.nextInt(3) + 5;
        boolean z2 = true;
        if (y < 1 || y + nextInt + 1 > 128) {
            return false;
        }
        for (int i = y; i <= y + 1 + nextInt; i++) {
            int i2 = i == y ? 0 : 1;
            if (i >= ((y + 1) + nextInt) - 2) {
                i2 = 2;
            }
            for (int i3 = x - i2; i3 <= x + i2 && z2; i3++) {
                for (int i4 = z - i2; i4 <= z + i2 && z2; i4++) {
                    if (i >= 0 && i < 128) {
                        int blockIDAt = world.getBlockIDAt(i3, i, i4);
                        if (blockIDAt != 0 && blockIDAt != Block.leaves.blockID) {
                            z2 = false;
                        }
                    } else {
                        z2 = false;
                    }
                }
            }
        }
        if (z2) {
            int blockIDAt2 = world.getBlockIDAt(x, y - 1, z);
            if ((blockIDAt2 == Block.grass.blockID || blockIDAt2 == Block.dirt.blockID) && y < (128 - nextInt) - 1) {
                world.placeBlock(x, y - 1, z, (byte) Block.dirt.blockID);
                for (int i5 = (y - 3) + nextInt; i5 <= y + nextInt; i5++) {
                    int i6 = i5 - (y + nextInt);
                    int i7 = 1 - (i6 / 2);
                    for (int i8 = x - i7; i8 <= x + i7; i8++) {
                        int i9 = i8 - x;
                        for (int i10 = z - i7; i10 <= z + i7; i10++) {
                            int i11 = i10 - z;
                            if ((Math.abs(i9) != i7 || Math.abs(i11) != i7 || (rand.nextInt(2) != 0 && i6 != 0))/* && !Block.FULL_OPAQUE[world.getBlockIDAt(i8, i5, i10)]TODO opaque*/) {
                                world.placeBlock(i8, i5, i10, (byte)Block.leaves.blockID, (byte)2);
                            }
                        }
                    }
                }
                for (int i12 = 0; i12 < nextInt; i12++) {
                    int blockIDAt3 = world.getBlockIDAt(x, y + i12, z);
                    if (blockIDAt3 == 0 || blockIDAt3 == Block.leaves.blockID) {
                        world.placeBlock(x, y + i12, z, (byte)Block.log.blockID, (byte)2);
                    }
                }
                return true;
            }
            return false;
        }
        return false;

	}

}
