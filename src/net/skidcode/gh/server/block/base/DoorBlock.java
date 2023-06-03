package net.skidcode.gh.server.block.base;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;

public class DoorBlock extends Block{

	public DoorBlock(int id, Material m) {
		super(id, m);
		this.isSolid = false;
	}

}
