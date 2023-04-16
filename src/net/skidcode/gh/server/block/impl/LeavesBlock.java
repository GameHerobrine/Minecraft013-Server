package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class LeavesBlock extends SolidBlock{

	public LeavesBlock(int id) {
		super(id, Material.leaves);
		this.name = "Leaves"; //TODO log meta: 0 - oak, 1 - spruce, 2 - birch
	}

}
