package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.OreBlock;
import net.skidcode.gh.server.block.material.Material;

public class GoldOreBlock extends OreBlock{

	public GoldOreBlock(int id) {
		super(id, Material.stone);
		this.name = "Gold Ore";
	}

}
