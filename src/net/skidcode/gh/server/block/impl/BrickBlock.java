package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class BrickBlock extends SolidBlock{

	public BrickBlock(int id) {
		super(id, Material.stone);
	}

}
