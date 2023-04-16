package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class LogBlock extends SolidBlock{

	public LogBlock(int id) {
		super(id, Material.wood);
		this.name = "Log"; //TODO log meta: 0 - oak, 1 - spruce, 2 - birch
	}

}
