package net.skidcode.gh.server.block.base;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.World;

public class LiquidStaticBlock extends LiquidBaseBlock{
	
	public LiquidStaticBlock(int id, Material m) {
		super(id, m);
		this.isSolid = false;
		Block.shouldTick[id] = (m == Material.lava);
	}
	
	public void setDynamic(World world, int x, int y, int z) {
		int meta = world.getBlockMetaAt(x, y, z);
		world.editingBlocks = true;
		world.placeBlock(x, y, z, (byte)(this.blockID - 1), (byte) meta);
		world.addToTickNextTick(x, y, z, this.blockID - 1, this.tickDelay);
		world.editingBlocks = false;
	}
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		/**
		 * if ( *((_DWORD *)this + 0x10) == Material::lava )
		    return (LiquidTile *)LiquidTile::_trySpreadFire(this, a2, a3, a4, a5, a6);
		  return this;
		 */
		//TODO fire spread
	}
	@Override
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {
		super.onNeighborBlockChanged(world, x, y, z, meta);
		if(this.blockID == world.getBlockIDAt(x, y, z)) this.setDynamic(world, x, y, z);
	}
	
}
