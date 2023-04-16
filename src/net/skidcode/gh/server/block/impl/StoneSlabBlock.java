package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.World;

public class StoneSlabBlock extends SolidBlock{

	public StoneSlabBlock(int id) {
		super(id, Material.stone);
	}
	
	public void onBlockPlacedByPlayer(World world, int x, int y, int z, int face, Player player) {
		if(world.getBlockIDAt(x, y - 1, z) == this.blockID) {
			Block.fullStoneSlab.onBlockPlacedByPlayer(world, x, y - 1, z, face, player);
		}else {
			super.onBlockPlacedByPlayer(world, x, y, z, face, player);
		}
	}
	
}
