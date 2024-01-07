package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class ReedBlock extends Block{

	public ReedBlock(int id) {
		super(id, Material.plant);
		this.setShape(0.5f - 0.375f, 0, 0.5f - 0.375f, 0.5f + 0.375f, 1, 0.5f + 0.375f);
		this.setTicking(true);
	}
	//TODO methods
}
