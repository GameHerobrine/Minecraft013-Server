package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class GlassBlock extends SolidBlock{

	public GlassBlock(int id) {
		super(id, Material.glass);
		this.name = "Glass";
	}

}
