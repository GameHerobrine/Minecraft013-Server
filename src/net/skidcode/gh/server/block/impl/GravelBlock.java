package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class GravelBlock extends SolidBlock{

	public GravelBlock(int id) {
		super(id, Material.sand);
		this.name = "Gravel";
	}

}
