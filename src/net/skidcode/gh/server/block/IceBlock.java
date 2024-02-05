package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class IceBlock extends HalfTransparentBlock{

	public IceBlock(int id) {
		super(id, Material.ice, false);
		this.slipperiness = 0.98f;
		this.setTicking(true);
	}
	//TODO methods
}
