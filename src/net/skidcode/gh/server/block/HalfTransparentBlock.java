package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class HalfTransparentBlock extends Block{
	public HalfTransparentBlock(int id, Material m, boolean f6c) {
		super(id, m);
		this.unkField_6c = f6c;
	}
	
	public boolean isSolidRender() {
		return false;
	}
}
