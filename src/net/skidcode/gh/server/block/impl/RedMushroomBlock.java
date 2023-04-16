package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.PlantBlock;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.world.World;

public class RedMushroomBlock extends PlantBlock{

	public RedMushroomBlock(int id) {
		super(id, Material.plant);
	}
}
