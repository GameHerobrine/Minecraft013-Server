package net.skidcode.gh.server.world.feature;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class FlowerFeature extends Feature{
	public byte blockID;
	public FlowerFeature(int id) {
		this.blockID = (byte) id;
	}
	
	@Override
	public boolean place(World world, BedrockRandom rand, int x, int y, int z) {
		for (int i = 0; i < 64; i++) {
			int xPos = (x + rand.nextInt(8)) - rand.nextInt(8);
			int yPos = (y + rand.nextInt(4)) - rand.nextInt(4);
			int zPos = (z + rand.nextInt(8)) - rand.nextInt(8);
			if (world.isAirBlock(xPos, yPos, zPos) && Block.blocks[this.blockID].canSurvive(world, xPos, yPos, zPos)) {
				world.placeBlock(xPos, yPos, zPos, this.blockID);
			}
		}
		return true;

	}
	
}
