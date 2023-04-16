package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class IronBlock extends SolidBlock{

	public IronBlock(int id) {
		super(id, Material.metal);
		
	}

}
