package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.DoorBlock;
import net.skidcode.gh.server.block.material.Material;

public class IronDoorBlock extends DoorBlock{

	public IronDoorBlock(int id) {
		super(id, Material.metal);
		this.name = "Iron Door";
	}

}
