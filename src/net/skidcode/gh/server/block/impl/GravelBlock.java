package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.FallingBlock;
import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class GravelBlock extends FallingBlock{

	public GravelBlock(int id) {
		super(id, Material.sand);
		this.name = "Gravel";
	}

}
