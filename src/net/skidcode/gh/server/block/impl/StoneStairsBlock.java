package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class StoneStairsBlock extends SolidBlock{ //TODO a baseclass for Stairs?

	public StoneStairsBlock(int id) {
		super(id, Material.stone);
		this.name = "Stone Stairs";
	}

}
