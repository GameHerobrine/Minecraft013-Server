package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class SpruceFeature extends Feature{

	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		int i;
        int nextInt = rand.nextInt(4) + 6;
        int nextInt2 = 1 + rand.nextInt(2);
        int i2 = nextInt - nextInt2;
        int nextInt3 = 2 + rand.nextInt(2);
        boolean z2 = true;
        if (y < 1 || y + nextInt + 1 > 128) {
            return false;
        }
        for (int i3 = y; i3 <= y + 1 + nextInt && z2; i3++) {
            if (i3 - y < nextInt2) {
                i = 0;
            } else {
                i = nextInt3;
            }
            for (int i4 = x - i; i4 <= x + i && z2; i4++) {
                for (int i5 = z - i; i5 <= z + i && z2; i5++) {
                    if (i3 >= 0 && i3 < 128) {
                        int blockIDAt = world.getBlockIDAt(i4, i3, i5);
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
                world.placeBlock(x, y - 1, z, (byte)Block.dirt.blockID);
                int nextInt4 = rand.nextInt(2);
                int i6 = 1;
                int i7 = 0;
                for (int i8 = 0; i8 <= i2; i8++) {
                    int i9 = (y + nextInt) - i8;
                    for (int i10 = x - nextInt4; i10 <= x + nextInt4; i10++) {
                        int i11 = i10 - x;
                        for (int i12 = z - nextInt4; i12 <= z + nextInt4; i12++) {
                            int i13 = i12 - z;
                            if ((Math.abs(i11) != nextInt4 || Math.abs(i13) != nextInt4 || nextInt4 <= 0) && !Block.solid[world.getBlockIDAt(i10, i9, i12)]) {
                                world.placeBlock(i10, i9, i12, (byte)Block.leaves.blockID, (byte)1);
                            }
                        }
                    }
                    if (nextInt4 >= i6) {
                        nextInt4 = i7;
                        i7 = 1;
                        i6++;
                        if (i6 > nextInt3) {
                            i6 = nextInt3;
                        }
                    } else {
                        nextInt4++;
                    }
                }
                int nextInt5 = rand.nextInt(3);
                for (int i14 = 0; i14 < nextInt - nextInt5; i14++) {
                    int blockIDAt3 = world.getBlockIDAt(x, y + i14, z);
                    if (blockIDAt3 == 0 || blockIDAt3 == Block.leaves.blockID) {
                        world.placeBlock(x, y + i14, z, (byte)Block.log.blockID, (byte)1);
                    }
                }
                return true;
            }
            return false;
        }
        return false;

	}

}
