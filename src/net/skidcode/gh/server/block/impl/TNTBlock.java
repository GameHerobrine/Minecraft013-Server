package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class TNTBlock extends SolidBlock{
	public TNTBlock(int id) {
		super(id, Material.explosive);
		this.name = "TNT";
	}
	
}
