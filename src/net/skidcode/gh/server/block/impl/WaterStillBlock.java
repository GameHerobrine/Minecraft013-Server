package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.LiquidStaticBlock;
import net.skidcode.gh.server.block.material.Material;

public class WaterStillBlock extends LiquidStaticBlock{

	public WaterStillBlock(int id) {
		super(id, Material.water);
		this.name = "Still Water";
		this.tickDelay = 5;
	}

}
