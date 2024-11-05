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
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		if(this != Block.stoneSlab) super.onBlockAdded(world, x, y, z);
		//gib enchantile @realfreehij
		int idb = world.getBlockIDAt(x, y-1, z);
		int datab = world.getBlockMetaAt(x, y-1, z);
		int data = world.getBlockMetaAt(x, y, z);
		if(data == datab && idb == Block.stoneSlab.blockID) {
			world.setBlock(x, y, z, 0, 0, 3);
			world.setBlock(x, y-1, z, Block.fullStoneSlab.blockID, data, 3);
		}
	}
	
	public boolean isSolidRender() {
		return this.isFullTile; //TODO check
	}
}
