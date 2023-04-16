package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class CactusBlock extends SolidBlock{

	public CactusBlock(int id) {
		super(id, Material.cactus);
		this.name = "Cactus";
	}

}
