package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class ReedBlock extends Block{

	public ReedBlock(int id) {
		super(id, Material.plant);
		this.setShape(0.5f - 0.375f, 0, 0.5f - 0.375f, 0.5f + 0.375f, 1, 0.5f + 0.375f);
		this.setTicking(true);
	}
	//TODO methods
	public boolean isSolidRender() {
		return false;
	}
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {
		this.checkAlive(world, x, y, z);
	}
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		//Logger.info(x+":"+y+":"+z+"  is"+this.blockID);
		if(world.isAirBlock(x, y + 1, z)) {
			int l = 0;
			do{
				l++;
			}while(world.getBlockIDAt(x, y - l, z) == this.blockID);
			
			if(l < 3) {
				int meta = world.getBlockMetaAt(x, y, z);
				if(meta == 15) {
					world.placeBlockAndNotifyNearby(x, y + 1, z, (byte) this.blockID);
					world.placeBlockMetaAndNotifyNearby(x, y, z, (byte)0);
				}else {
					world.placeBlockMetaAndNotifyNearby(x, y, z, (byte) (meta + 1)); //same as cacti
				}
			}
		}
	}
	//TODO getAABB -> null
	
	public void checkAlive(World world, int x, int y, int z) {
		if(!this.canSurvive(world, x, y, z)) world.setBlock(x, y, z, (byte)0, (byte)0, 3);
	}
	
	public boolean mayPlace(World world, int x, int y, int z) {
		int idBelow = world.getBlockIDAt(x, y - 1, z);
		
		if(idBelow == this.blockID) return true;
		if(idBelow != Block.grass.blockID && idBelow != Block.dirt.blockID) return false;
		
		if(world.getMaterial(x - 1, y - 1, z) == Material.water) return true;
		if(world.getMaterial(x + 1, y - 1, z) == Material.water) return true;
		if(world.getMaterial(x, y - 1, z - 1) == Material.water) return true;
		
		return world.getMaterial(x, y - 1, z + 1) == Material.water;
		
	}
	public boolean canSurvive(World world, int x, int y, int z) {
		return this.mayPlace(world, x, y, z);
	}
}
