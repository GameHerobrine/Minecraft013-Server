package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class DoorBlock extends Block{

	public DoorBlock(int id, Material m) {
		super(id, m);
		this.setShape(0.5f - 0.5f, 0.0f, 0.5f - 0.5f, 0.5f + 0.5f, 1.0f, 0.5f + 0.5f);
	}
	//TODO methods
	
	public boolean isSolidRender() {
		return false;
	}
}
