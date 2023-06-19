package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.PlantBlock;
import net.skidcode.gh.server.block.material.Material;

public class RoseBlock extends PlantBlock{

	public RoseBlock(int id) {
		super(id, Material.plant);
		this.name = "Rose";
		Block.shouldTick[id] = true;
	}
	
}
