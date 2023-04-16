package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class CobblestoneBlock extends SolidBlock{

	public CobblestoneBlock(int id) {
		super(id, Material.stone);
		this.name = "Cobblestone";
	}

}
