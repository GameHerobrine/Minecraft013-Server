package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class StoneSlabBlock extends Block{

	public StoneSlabBlock(int id, boolean isFull) {
		super(id, Material.stone);
		this.isFullTile = isFull;
		if(!isFull) this.setShape(0, 0, 0, 1, 0.5f, 1);
		this.setLightBlock(255);
	}
	
	public boolean isSolidRender() {
		return this.isFullTile; //TODO check
	}
}
