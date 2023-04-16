package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class InfoUpdate2Block extends SolidBlock{
	public InfoUpdate2Block(int id) {
		super(id, Material.dirt);
		this.name = "Info Update 2";
	}
}
