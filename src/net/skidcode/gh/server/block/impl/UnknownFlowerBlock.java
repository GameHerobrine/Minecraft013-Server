package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.PlantBlock;
import net.skidcode.gh.server.block.material.Material;

public class UnknownFlowerBlock extends PlantBlock{

	public UnknownFlowerBlock(int id) {
		super(id, Material.plant);
	}
	
}
