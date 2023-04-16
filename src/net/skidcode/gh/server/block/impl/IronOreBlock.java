package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.OreBlock;
import net.skidcode.gh.server.block.material.Material;

public class IronOreBlock extends OreBlock{

	public IronOreBlock(int id) {
		super(id, Material.stone);
		this.name = "Iron Ore";
	}

}
