package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class InfoUpdateBlock extends SolidBlock{

	public InfoUpdateBlock(int id) {
		super(id, Material.dirt);
		this.name = "Info Update";
	}

}
