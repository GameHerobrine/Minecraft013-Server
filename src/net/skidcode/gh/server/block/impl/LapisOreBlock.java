package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.OreBlock;
import net.skidcode.gh.server.block.material.Material;

public class LapisOreBlock extends OreBlock{

	public LapisOreBlock(int id) {
		super(id, Material.stone);
		this.name = "Lapis Ore";
	}

}
