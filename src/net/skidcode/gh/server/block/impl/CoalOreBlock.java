package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.OreBlock;
import net.skidcode.gh.server.block.material.Material;

public class CoalOreBlock extends OreBlock{

	public CoalOreBlock(int id) {
		super(id, Material.stone);
		this.name = "Coal Ore";
	}

}
