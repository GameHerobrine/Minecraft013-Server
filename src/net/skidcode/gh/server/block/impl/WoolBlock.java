package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class WoolBlock extends SolidBlock{

	public WoolBlock(int id, int color) {
		super(id, Material.cloth);
	}

}
