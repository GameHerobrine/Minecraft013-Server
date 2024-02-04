package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.World;

public class StoneSlabBlock extends Block{

	public StoneSlabBlock(int id, boolean isFull) {
		super(id, Material.stone);
		this.isFullTile = isFull;
		if(!isFull) this.setShape(0, 0, 0, 1, 0.5f, 1);
		this.setLightBlock(255);
	}
	
	public void onBlockPlacedByPlayer(World world, int x, int y, int z, int face, Player player) {
		if(world.getBlockIDAt(x, y - 1, z) == this.blockID) {
			Block.fullStoneSlab.onBlockPlacedByPlayer(world, x, y - 1, z, face, player);
		}else {
			super.onBlockPlacedByPlayer(world, x, y, z, face, player);
		}
	}
	
	public boolean isSolidRender() {
		return this.isFullTile; //TODO check
	}
}
