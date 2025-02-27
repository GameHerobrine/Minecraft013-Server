package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;

public class OreBlock extends Block{

	public OreBlock(int id) {
		super(id, Material.stone);
	}

	@Override
	public int getResource(int meta, BedrockRandom random) {
		return this.blockID;
	}
	@Override
	public int getResourceCount(BedrockRandom random) {
		if(this.blockID == Block.lapisOre.blockID) {
			return random.nextInt(5) + 4;
		}
		return 1;
	}
	@Override
	public int getSpawnResourcesAuxValue(int meta) {
		return 0;
	}
}
