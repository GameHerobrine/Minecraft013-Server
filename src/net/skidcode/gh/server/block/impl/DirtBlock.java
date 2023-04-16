package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class DirtBlock extends SolidBlock{

	public DirtBlock(int id) {
		super(id, Material.dirt);
		this.name = "Dirt";
	}

}
