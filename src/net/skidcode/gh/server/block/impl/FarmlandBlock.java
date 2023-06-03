package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class FarmlandBlock extends SolidBlock{

	public FarmlandBlock(int id) {
		super(id, Material.dirt);
		this.isSolid = false;
	}

}
