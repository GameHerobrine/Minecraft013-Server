package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class CactusBlock extends Block{

	public CactusBlock(int id) {
		super(id, Material.cactus);
		this.setTicking(true);
	}
	//TODO methods
}
