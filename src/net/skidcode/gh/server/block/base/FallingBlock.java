package net.skidcode.gh.server.block.base;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class FallingBlock extends SolidBlock{
	
	public static boolean instaFall = false;
	
	public FallingBlock(int id, Material m) {
		super(id, m);
		this.tickDelay = 3;
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		this.checkSlide(world, x, y, z);
	}

	public void checkSlide(World world, int x, int y, int z) {
		if(y >= 0 && this.isFree(world, x, y, z)) {
			if(FallingBlock.instaFall || world.hasChunksAt(x - 32, y - 32, z - 32, x + 32, y + 32, z + 32)) {
				world.placeBlock(x, y, z, (byte) 0);
				boolean b;
				do {
					b = this.isFree(world, x, y - 1, z);
				}while(b || --y > 0);
				
				if(y > 0) {
					world.placeBlock(x, y, z, (byte) this.blockID);
				}
			}else {
				//entity TODO 0.2?
			}
		}
	}
	
	public boolean isFree(World world, int x, int y, int z) {
		int id = world.getBlockIDAt(x, y, z);
		if(id == 0 || id == Block.fire.blockID) return true;
		
		Material m = Block.blocks[id].material;
		return m == Material.water || m == Material.lava;
	}
	
}
