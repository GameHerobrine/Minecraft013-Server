package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class GrassBlock extends SolidBlock{

	public GrassBlock(int id) {
		super(id, Material.dirt);
		this.name = "Grass";
	}

}
