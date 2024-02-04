package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class FarmBlock extends Block{

	public FarmBlock(int id) {
		super(id, Material.dirt);
		this.setTicking(true);
		this.setShape(0.0f, 0.0f, 0.0f, 1.0f, 0.9375f, 1.0f);
		this.setLightBlock(255);
	}
	//TODO methods
	
	public boolean isSolidRender() {
		return false;
	}
}
