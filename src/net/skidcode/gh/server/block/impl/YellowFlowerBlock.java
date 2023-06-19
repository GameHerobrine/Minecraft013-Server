package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.PlantBlock;
import net.skidcode.gh.server.block.material.Material;

public class YellowFlowerBlock extends PlantBlock{

	public YellowFlowerBlock(int id) {
		super(id, Material.plant);
		Block.shouldTick[id] = true;
	}
	
}
