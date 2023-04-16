package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.LiquidFlowingBlock;
import net.skidcode.gh.server.block.material.Material;

public class LavaBlock extends LiquidFlowingBlock{
	public LavaBlock(int id) {
		super(id, Material.lava);
		this.name = "Lava";
	}

}
