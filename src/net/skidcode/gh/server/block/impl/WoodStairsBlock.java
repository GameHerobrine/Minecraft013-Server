package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class WoodStairsBlock extends SolidBlock{

	public WoodStairsBlock(int id) {
		super(id, Material.wood);
	}

}
