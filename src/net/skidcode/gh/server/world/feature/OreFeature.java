package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.MathUtils;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class OreFeature extends Feature{
	public byte oreID;
	public int amount;
	
	public OreFeature(int oreID, int amount) {
		this.oreID = (byte) oreID;
		this.amount = amount;
	}

	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		float nextFloat = rand.nextFloat() * 3.1416f;
        float sin = (float) (x + 8 + ((MathUtils.sin(nextFloat) * this.amount) / 8.0f));
        float sin2 = (float) ((x + 8) - ((MathUtils.sin(nextFloat) * this.amount) / 8.0f));
        float cos = (float) (z + 8 + ((MathUtils.cos(nextFloat) * this.amount) / 8.0f));
        float cos2 = (float) ((z + 8) - ((MathUtils.cos(nextFloat) * this.amount) / 8.0f));
        float nextInt = y + rand.nextInt(3) + 2;
        float nextInt2 = y + rand.nextInt(3) + 2;
        for (int i = 0; i <= this.amount; i++) {
            float d = sin + (((sin2 - sin) * i) / this.amount);
            float d2 = nextInt + (((nextInt2 - nextInt) * i) / this.amount);
            float d3 = cos + (((cos2 - cos) * i) / this.amount);
            nextFloat = (rand.nextFloat() * this.amount) / 16.0f;
            float sin3 = (float) (((MathUtils.sin((i * 3.1416f) / this.amount) + 1.0f) * nextFloat) + 1.0f);
            float sin4 = (float) (((MathUtils.sin((i * 3.1416f) / this.amount) + 1.0f) * nextFloat) + 1.0f);
            int floor = MathUtils.ffloor(d - (sin3 / 2.0f));
            int floor2 = MathUtils.ffloor(d2 - (sin4 / 2.0f));
            int floor3 = MathUtils.ffloor(d3 - (sin3 / 2.0f));
            int floor4 = MathUtils.ffloor(d + (sin3 / 2.0f));
            int floor5 = MathUtils.ffloor(d2 + (sin4 / 2.0f));
            int floor6 = MathUtils.ffloor(d3 + (sin3 / 2.0f));
            for (int i2 = floor; i2 <= floor4; i2++) {
                float d4 = ((i2 + 0.5f) - d) / (sin3 / 2.0f);
                if (d4 * d4 < 1.0d) {
                    for (int i3 = floor2; i3 <= floor5; i3++) {
                        float d5 = ((i3 + 0.5f) - d2) / (sin4 / 2.0f);
                        if ((d4 * d4) + (d5 * d5) < 1.0f) {
                            for (int i4 = floor3; i4 <= floor6; i4++) {
                                float d6 = ((i4 + 0.5f) - d3) / (sin3 / 2.0f);
                                if ((d4 * d4) + (d5 * d5) + (d6 * d6) < 1.0f && world.getBlockIDAt(i2, i3, i4) == Block.stone.blockID) {
                                	world.placeBlock(i2, i3, i4, this.oreID);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
	}
	
	
	
}
