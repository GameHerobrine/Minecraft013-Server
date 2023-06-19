package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.PlantBlock;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class ReedsBlock extends PlantBlock{

	public ReedsBlock(int id) {
		super(id, Material.plant);
		this.isSolid = false;
		Block.shouldTick[id] = true;
	}
	
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		Logger.info(x+":"+y+":"+z+"  is"+this.blockID);
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
