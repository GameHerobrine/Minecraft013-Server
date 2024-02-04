package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class FireBlock extends Block{
	
	public int burnOdds[] = new int[256];
	public int igniteOdds[] = new int[256];
	
	public FireBlock(int id) {
		super(id, Material.fire);
		this.setFlammable(Block.wood.blockID, 5, 20);
		this.setFlammable(Block.log.blockID, 5, 5);
		this.setFlammable(Block.leaves.blockID, 30, 60);
		this.setFlammable(Block.tnt.blockID, 15, 100);
		this.setFlammable(Block.wool.blockID, 30, 60);
		this.setTicking(true);
		
	}

	public void setFlammable(int id, int igniteOdd, int burnOdd) {
		this.burnOdds[id] = burnOdd;
		this.igniteOdds[id] = igniteOdd;
	}
	
	public boolean isSolidRender() {
		return false;
	}
	
	public int getTickDelay() {
		return 30;
	}
	
}
