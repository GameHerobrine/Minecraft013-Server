package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.MathUtils;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class ClayFeature extends Feature{
	public int size;
	public byte clayID = (byte) Block.clay.blockID;
	public ClayFeature(int size) {
		this.size = size;
	}
	
	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		
		if(world.getMaterial(x, y, z) != Material.water) {
			return false;
		}
		float nextFloat = rand.nextFloat() * 3.1416f;
		float sin = (float) (x + 8 + ((Math.sin(nextFloat) * this.size) / 8.0f));
		float sin2 = (float) ((x + 8) - ((Math.sin(nextFloat) * this.size) / 8.0f));
		float cos = (float) (z + 8 + ((Math.cos(nextFloat) * this.size) / 8.0f));
		float cos2 = (float) ((z + 8) - ((Math.cos(nextFloat) * this.size) / 8.0f));
		float nextInt = y + rand.nextInt(3) + 2;
		float nextInt2 = y + rand.nextInt(3) + 2;
		for (int i = 0; i <= this.size; i++) {
			float d = sin + (((sin2 - sin) * i) / this.size);
			float d2 = nextInt + (((nextInt2 - nextInt) * i) / this.size);
			float d3 = cos + (((cos2 - cos) * i) / this.size);
			nextFloat = (rand.nextFloat() * this.size) / 16.0f;
			float sin3 = (float) (((Math.sin((i * 3.1416f) / this.size) + 1.0f) * nextFloat) + 1.0f);
			float sin4 = (float) (((Math.sin((i * 3.1416f) / this.size) + 1.0f) * nextFloat) + 1.0f);
			int floor = MathUtils.ffloor(d - (sin3 / 2.0f)); //TODO Mth
			int floor2 = MathUtils.ffloor(d + (sin3 / 2.0f));
			int floor3 = MathUtils.ffloor(d2 - (sin4 / 2.0f));
			int floor4 = MathUtils.ffloor(d2 + (sin4 / 2.0f));
			int floor5 = MathUtils.ffloor(d3 - (sin3 / 2.0f));
			int floor6 = MathUtils.ffloor(d3 + (sin3 / 2.0f));
			for (int i2 = floor; i2 <= floor2; i2++) {
				for (int i3 = floor3; i3 <= floor4; i3++) {
					for (int i4 = floor5; i4 <= floor6; i4++) {
						float d4 = ((i2 + 0.5f) - d) / (sin3 / 2.0f);
						float d5 = ((i3 + 0.5f) - d2) / (sin4 / 2.0f);
						float d6 = ((i4 + 0.5f) - d3) / (sin3 / 2.0f);
						if ((d4 * d4) + (d5 * d5) + (d6 * d6) < 1 && world.getBlockIDAt(i2, i3, i4) == Block.sand.blockID) {
							world.placeBlock(i2, i3, i4, this.clayID);
						}
					}
				}
			}
		}

		return true;
	}

}
