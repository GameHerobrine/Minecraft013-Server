package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class GrassBlock extends Block{

	public GrassBlock(int id) {
		super(id, Material.dirt);
		//TODO properties
		//*((_DWORD *)this + 1) = 3;
		this.unkField_4 = 3;
		this.setTicking(true);
		
	}

}
