package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class SandBlock extends Block{

	public SandBlock(int id) {
		super(id, Material.sand);
	}
	
	public int getTickDelay() {
		return 3;
	}
	//TODO special methods
}
