package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.StairsBlock;
import net.skidcode.gh.server.block.material.Material;

public class StoneStairsBlock extends StairsBlock{

	public StoneStairsBlock(int id) {
		super(id, Material.stone);
		this.name = "Stone Stairs";
	}

}
