package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class StoneBlock extends SolidBlock{

	public StoneBlock(int id) {
		super(id, Material.stone);
		this.name = "Stone";
	}

}
