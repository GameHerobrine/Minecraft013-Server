package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class DiamondBlock extends SolidBlock{

	public DiamondBlock(int id) {
		super(id, Material.metal);
		this.name = "Diamond Block";
	}

}
