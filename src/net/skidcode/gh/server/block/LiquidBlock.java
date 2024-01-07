package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class LiquidBlock extends Block{

	public LiquidBlock(int id, Material m) {
		super(id, m);
		
		//Tile::setShape((Tile *)this, 0.0 + 0.0, 0.0 + 0.0, 0.0 + 0.0, 0.0 + 1.0, 0.0 + 1.0, 0.0 + 1.0);
		this.setTicking(true); //TODO setShape
	}

}
