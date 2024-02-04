package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class TorchBlock extends Block{

	public TorchBlock(int id) {
		super(id, Material.decoration);
		this.setTicking(true); //TODO ticking might be used only for clientside rendering
	}
	
	public boolean isSolidRender() {
		return false;
	}
}
