package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class FullStoneSlabBlock extends SolidBlock{

	public FullStoneSlabBlock(int id) {
		super(id, Material.stone);
	}

}
