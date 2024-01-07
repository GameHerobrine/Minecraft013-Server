package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class BushBlock extends Block{

	public BushBlock(int id) {
		super(id, Material.plant);
		this.setTicking(true);
		this.setShape(0.5f - 0.2f, 0.0f, 0.5f - 0.2f, 0.2f + 0.5f, 0.2f*3.0f, 0.2f+0.5f);
	}
	//TODO more methods
}
