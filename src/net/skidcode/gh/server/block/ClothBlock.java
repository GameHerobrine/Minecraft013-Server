package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class ClothBlock extends Block{
	public int color;
	public ClothBlock(int id, int color) {
		super(id, Material.cloth);
		this.color = color;
	}

	@Override
	public int getSpawnResourcesAuxValue(int meta) {
		return meta;
	}
	
}
