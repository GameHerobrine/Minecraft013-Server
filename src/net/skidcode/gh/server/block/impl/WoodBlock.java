package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class WoodBlock extends SolidBlock{

	public WoodBlock(int id) {
		super(id, Material.wood);
		this.name = "Wood";
	}

}
