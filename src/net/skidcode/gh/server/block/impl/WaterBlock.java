package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.LiquidFlowingBlock;
import net.skidcode.gh.server.block.material.Material;

public class WaterBlock extends LiquidFlowingBlock{

	public WaterBlock(int id) {
		super(id, Material.water);
		this.name = "Water";
		this.tickrate = 5;
	}

}
