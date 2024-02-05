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
	
	public boolean canSurvive(World world, int x, int y, int z) {
		
		int dY = y - 1;
		int idDown = world.getBlockIDAt(x, dY, z);
		if(idDown == this.blockID) return true;
		else if(idDown != Block.grass.blockID && idDown != Block.dirt.blockID) return false;
		return world.getMaterial(x - 1, dY, z) == Material.water || world.getMaterial(x + 1, dY, z) == Material.water || world.getMaterial(x, dY, z - 1) == Material.water || world.getMaterial(x, dY, z + 1) == Material.water;
	}
}
