package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;

public class LadderBlock extends Block{

	public LadderBlock(int id) {
		super(id, Material.decoration);
	}

}
