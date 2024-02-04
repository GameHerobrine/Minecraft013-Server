package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.world.World;

public class LiquidBlockStatic extends LiquidBlock{

	public LiquidBlockStatic(int id, Material m) {
		super(id, m);
		this.setTicking(m == Material.lava);
	}
	
	public void setDynamic(World world, int x, int y, int z) {
		int meta = world.getBlockMetaAt(x, y, z);
		world.editingBlocks = true;
		world.placeBlock(x, y, z, (byte)(this.blockID - 1), (byte) meta);
		world.addToTickNextTick(x, y, z, this.blockID - 1, this.getTickDelay());
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
