package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;

public class FireBlock extends Block{

	public FireBlock(int id) {
		super(id, Material.fire);
	}

}
