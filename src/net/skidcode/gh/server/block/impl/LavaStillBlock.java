package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.LiquidStaticBlock;
import net.skidcode.gh.server.block.material.Material;

public class LavaStillBlock extends LiquidStaticBlock{
	public LavaStillBlock(int id) {
		super(id, Material.lava);
		this.name = "Still Lava";
		this.tickrate = 30;
	}

}
