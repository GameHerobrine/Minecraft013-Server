package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class StoneSlabBlock extends Block{

	public StoneSlabBlock(int id, boolean isFull) {
		super(id, Material.stone);
		this.isFullTile = isFull;
		if(!isFull) this.setShape(0, 0, 0, 1, 0.5f, 1);
		this.setLightBlock(255);
	}
	
	@Override
	public void onPlace(World world, int x, int y, int z) {
		if(this != Block.stoneSlab) super.onPlace(world, x, y, z);
		//gib enchantile @realfreehij
		int idb = world.getBlockIDAt(x, y-1, z);
		int datab = world.getBlockMetaAt(x, y-1, z);
		int data = world.getBlockMetaAt(x, y, z);
		if(data == datab && idb == Block.stoneSlab.blockID) {
			world.setBlock(x, y, z, 0, 0, 3);
			world.setBlock(x, y-1, z, Block.fullStoneSlab.blockID, data, 3);
		}
	}
	
	@Override
	public int getResource(int meta, BedrockRandom random) {
		return Block.stoneSlab.blockID;
	}
	@Override
	public int getResourceCount(BedrockRandom random) {
		return this.isFullTile ? 2 : 1;
	}
	@Override
	public int getSpawnResourcesAuxValue(int meta) {
		return meta;
	}
	
	@Override
	public boolean isSolidRender() {
		return this.isFullTile;
	}
}
