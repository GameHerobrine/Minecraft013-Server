package net.skidcode.gh.server.block.base;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class LiquidFlowingBlock extends Block{
	public int something = 0;
	public int tickrate = 0;
	public LiquidFlowingBlock(int id, Material m) {
		super(id, m);
		this.isSolid = false;
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		world.addToTickNextTick(x, y, z, this.blockID, this.tickrate);
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		int depth = this.getDepth(world, x, y, z);
		Logger.info("tickin");
		byte byte0 = (byte) (this.material == Material.lava ? 2 : 1); //prob flow speed?
		boolean flag = true;
		if(depth > 0) {
			int i1 = -100;
			this.something = 0;
			i1 = this.weirdfunc1(world, x - 1, y, z, i1);
			i1 = this.weirdfunc1(world, x + 1, y, z, i1);
			i1 = this.weirdfunc1(world, x, y, z - 1, i1);
			i1 = this.weirdfunc1(world, x, y, z + 1, i1);
			int j1 = i1 + byte0;
			if(j1 >= 8 || i1 < 0) j1 = -1;
			int l1 = this.getMetaForSelfMaterial(world, x, y + 1, z);
			if(l1 >= 0) {
				j1 = (l1 >= 8 ? l1 : l1 + 8);
			}
			if(this.something >= 2 && this.material == Material.water) {
				int idBot = world.getBlockIDAt(x, y - 1, z);
				if(idBot != 0) {
					Block b = Block.blocks[idBot];
					if(b.isSolid) j1 = 0;
					else if(b.material == this.material && world.getBlockMetaAt(x, y, z) == 0) {
						j1 = 0;
					}
				}
			}
			
			if(this.material == Material.lava && depth < 8 && j1 < 8 && j1 > depth && random.nextInt(4) != 0) {
				j1 = depth;
				flag = false;
			}
			if(j1 != depth) {
				depth = j1;
				if(depth < 0) {
					world.placeBlockAndNotifyNearby(x, y, z, (byte) 0);
				}else {
					world.placeBlockAndNotifyNearby(x, y, z, (byte) depth);
					world.addToTickNextTick(x, y, z, this.blockID, this.tickrate);
					world.notifyNearby(x, y, z);
				}
			}else if(flag) {
				this.placeSomething(world, x, y, z);
			}
		}else {
			this.placeSomething(world, x, y, z);
		}
	}
	
	public void placeSomething(World w, int x, int y, int z) { //TODO better name
		int meta = w.getBlockMetaAt(x, y, z);
		w.placeBlock(x, y, z, (byte) (this.blockID + 1), (byte) meta);
	}
	
	public int getMetaForSelfMaterial(World world, int x, int y, int z) {
		return world.getMaterial(x, y, z) != this.material ? -1 : world.getBlockMetaAt(x, y, z);
	}
	public int weirdfunc1(World world, int x, int y, int z, int u) {
		int metaself = this.getMetaForSelfMaterial(world, x, y, z);
		if(metaself < 0) return u;
		else if(metaself == 0) {
			this.something++;
		}else if(metaself >= 8) metaself = 0;
		
		return u >= 0 && metaself >= u ? u : metaself;
	}
	
	public int getDepth(World world, int x, int y, int z) {
		if(world.getMaterial(x, y, z) != this.material) return -1;
		return world.getBlockMetaAt(x, y, z);
	}
	
}
