package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class LeavesBlock extends SolidBlock{

	public LeavesBlock(int id) {
		super(id, Material.leaves);
		this.name = "Leaves";
		this.isSolid = false; //um well... it depends on graphics level...?
	}
}
