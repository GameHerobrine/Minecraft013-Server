package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.world.World;

public class LiquidBlock extends Block{

	public LiquidBlock(int id, Material m) {
		super(id, m);
		this.setShape(0, 0, 0, 1, 1, 1);
		this.setTicking(true);
	}
	
	public int getTickDelay() {
		return 10;
	}
	
	public boolean isSolidRender() {
		return false;
	}
	
	public void updateLiquid(World world, int x, int y, int z) {
		if(world.getBlockIDAt(x, y, z) == this.blockID && this.material == Material.lava && 
		(world.getMaterial(x, y, z - 1) == Material.water || world.getMaterial(x, y, z + 1) == Material.water ||
		world.getMaterial(x - 1, y, z) == Material.water || world.getMaterial(x + 1, y, z) == Material.water ||
		world.getMaterial(x, y + 1, z) == Material.water)) {
			
			int meta = world.getBlockMetaAt(x, y, z);
			if(meta == 0) {
				world.placeBlockAndNotifyNearby(x, y, z, (byte) Block.obsidian.blockID);
			}else if(meta <= 4) {
				world.placeBlockAndNotifyNearby(x, y, z, (byte) Block.cobblestone.blockID);
			}
			
		}
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		this.updateLiquid(world, x, y, z);
	}
	
	@Override
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {
		this.updateLiquid(world, x, y, z);
	}
}
