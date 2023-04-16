package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.OreBlock;
import net.skidcode.gh.server.block.material.Material;

public class GlowingRedstoneOreBlock extends OreBlock{

	public GlowingRedstoneOreBlock(int id) {
		super(id, Material.stone);
		this.name = "Redstone Ore";
	}

}
