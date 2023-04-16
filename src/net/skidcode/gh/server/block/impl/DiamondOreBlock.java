package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.OreBlock;
import net.skidcode.gh.server.block.material.Material;

public class DiamondOreBlock extends OreBlock{

	public DiamondOreBlock(int id) {
		super(id, Material.stone);
		this.name = "Diamond Ore";
	}

}
