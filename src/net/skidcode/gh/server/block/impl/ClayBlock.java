package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class ClayBlock extends SolidBlock{

	public ClayBlock(int id) {
		super(id, Material.clay);
		this.name = "Clay";
	}

}
