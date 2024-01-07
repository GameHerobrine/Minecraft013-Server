package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class RedstoneOreBlock extends Block{

	public RedstoneOreBlock(int id, boolean isTicking) {
		super(id, Material.stone);
		this.setTicking(isTicking);
		this.unkField_6c = isTicking;
		
		this.tickDelay = 30;
	}
	//TODO methods
}
