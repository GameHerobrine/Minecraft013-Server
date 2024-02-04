package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class DirtBlock extends Block{

	public DirtBlock(int id) {
		super(id, Material.dirt);
		this.setTicking(true);
	}

}
