package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.DoorBlock;
import net.skidcode.gh.server.block.material.Material;

public class WoodenDoorBlock extends DoorBlock{

	public WoodenDoorBlock(int id) {
		super(id, Material.wood);
	}

}
