package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class IceBlock extends SolidBlock{

	public IceBlock(int id) {
		super(id, Material.ice);
		this.name = "Ice";
		Block.shouldTick[id] = true;
	}

}
