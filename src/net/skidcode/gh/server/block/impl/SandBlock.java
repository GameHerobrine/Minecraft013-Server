package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.FallingBlock;
import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class SandBlock extends FallingBlock{

	public SandBlock(int id) {
		super(id, Material.sand);
		this.name = "Sand";
	}

}
