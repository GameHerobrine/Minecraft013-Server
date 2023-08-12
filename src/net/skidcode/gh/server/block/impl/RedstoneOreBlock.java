package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.OreBlock;
import net.skidcode.gh.server.block.material.Material;

public class RedstoneOreBlock extends OreBlock{

	public RedstoneOreBlock(int id) {
		super(id, Material.stone);
		this.name = "Redstone Ore";
		this.tickDelay = 30;
	}

}
