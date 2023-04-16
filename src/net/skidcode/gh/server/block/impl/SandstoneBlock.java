package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;

public class SandstoneBlock extends Block{

	public SandstoneBlock(int id) {
		super(id, Material.stone);
		this.name = "Sandstone";
	}

}
