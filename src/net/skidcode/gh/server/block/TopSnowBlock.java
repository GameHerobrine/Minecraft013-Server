package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class TopSnowBlock extends Block {

	public TopSnowBlock(int id) {
		super(id, Material.topSnow);
		this.setShape(0, 0, 0, 1, 0.125f, 1);
		this.setTicking(true);
	}
	//TODO methods
}
