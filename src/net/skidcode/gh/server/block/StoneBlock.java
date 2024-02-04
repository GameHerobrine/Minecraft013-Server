package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class StoneBlock extends Block{

	public StoneBlock(int id) {
		super(id, Material.stone);
		
		this.name = "stone";
	}
	
	//TODO StoneTile::getResource
}
