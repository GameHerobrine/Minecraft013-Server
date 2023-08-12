package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.OreBlock;
import net.skidcode.gh.server.block.material.Material;

public class GlowingRedstoneOreBlock extends OreBlock{ //TODO remove too many block classes

	public GlowingRedstoneOreBlock(int id) {
		super(id, Material.stone);
		this.name = "Redstone Ore";
		Block.shouldTick[id] = true;
		this.tickDelay = 30;
	}

}
