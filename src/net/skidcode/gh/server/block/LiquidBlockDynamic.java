package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class LiquidBlockDynamic extends LiquidBlock{
	
	public int adjacentLiquidSources = 0;
	public boolean[] canSpread = new boolean[4];
	public int[] dist = new int[4];
	
	public LiquidBlockDynamic(int id, Material m) {
		super(id, m);
		this.adjacentLiquidSources = 0;
	}
	
	@Override
	public void onPlace(World world, int x, int y, int z) {
		super.onPlace(world, x, y, z);
		if(world.getBlockIDAt(x, y, z) == this.blockID) {
			world.addToTickNextTick(x, y, z, this.blockID, this.getTickDelay());
		}
		
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		int depth = this.getDepth(world, x, y, z);
		byte byte0 = (byte) (this.material == Material.lava ? 2 : 1); //prob flow speed?
		boolean flag = true;
		if(depth > 0) {
			int i1 = -100;
			this.adjacentLiquidSources = 0;
			i1 = this.getHighest(world, x - 1, y, z, i1);
			i1 = this.getHighest(world, x + 1, y, z, i1);
			i1 = this.getHighest(world, x, y, z - 1, i1);
			i1 = this.getHighest(world, x, y, z + 1, i1);
			int j1 = i1 + byte0;
			if(j1 >= 8 || i1 < 0) j1 = -1;
			int l1 = this.getDepth(world, x, y + 1, z);
			if(l1 >= 0) {
				j1 = (l1 >= 8 ? l1 : l1 + 8);
			}
			if(this.adjacentLiquidSources >= 2 && this.material == Material.water) {
				int idBot = world.getBlockIDAt(x, y - 1, z);
				if(idBot != 0) {
					Block b = Block.blocks[idBot];
					if(b.isSolidRender() || (b.material == this.material && world.getBlockMetaAt(x, y, z) == 0)) j1 = 0;
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
					world.placeBlockMetaAndNotifyNearby(x, y, z, (byte) depth);
					world.addToTickNextTick(x, y, z, this.blockID, this.getTickDelay());
					world.notifyNearby(x, y, z, this.blockID);
				}
			}else if(flag) {
				this.setStatic(world, x, y, z);
			}
		}else {
			this.setStatic(world, x, y, z);
		}
		
		if(this.canSpreadTo(world, x, y - 1, z)) {
			world.placeBlockAndNotifyNearby(x, y - 1, z, (byte) this.blockID, (byte)((depth >= 8 ? depth : depth + 8) & 0xf));
		}else
		if(depth >= 0 && (depth == 0 || this.isWaterBlocking(world, x, y - 1, z))) {
			boolean[] flags = this.getSpread(world, x, y, z);
			int k1 = depth >= 8 ? 1 : (depth + byte0);
			if(k1 >= 8) return;
			if(flags[0]) this.trySpreadTo(world, x - 1, y, z, k1);
			if(flags[1]) this.trySpreadTo(world, x + 1, y, z, k1);
			if(flags[2]) this.trySpreadTo(world, x, y, z - 1, k1);
			if(flags[3]) this.trySpreadTo(world, x, y, z + 1, k1);
		}
	}
	
	public void trySpreadTo(World w, int x, int y, int z, int meta) {
		if(this.canSpreadTo(w, x, y, z)) {
			int i1 = w.getBlockIDAt(x, y, z);
			if(i1 > 0) {
				/*if(blockMaterial == Material.lava)
                {
                    fizz(world, i, j, k); //spawns 8 largesmoke particles
                }*/
				if(this.material != Material.lava) {
					//TODO in case of further updates Block.blocks[i1].drop();
				}
			}
			w.placeBlockAndNotifyNearby(x, y, z, (byte) this.blockID, (byte) meta);
		}
	}
	public boolean[] getSpread(World w, int x, int y, int z) {
		for(int l = 0; l < 4; ++l) {
			this.dist[l] = 1000;
			int j1 = x;
			int i2 = y;
			int j2 = z;
			switch(l) {
				case 0:
					--j1;
					break;
				case 1:
					++j1;
					break;
				case 2:
					--j2;
					break;
				case 3:
					++j2;
					break;
			}
			if(this.isWaterBlocking(w, j1, i2, j2) || w.getMaterial(j1, i2, j2) == this.material && w.getBlockMetaAt(j1, i2, j2) == 0) {
				continue;
			}
			if(!this.isWaterBlocking(w, j1, i2 - 1, j2)) {
				this.dist[l] = 0;
			}else {
				this.dist[l] = this.getSlopeDistance(w, j1, i2, j2, 1, l);
			}
		}
		
		int i1 = this.dist[0];
		for(int k1 = 1; k1 < 4; ++k1) {
			if(this.dist[k1] < i1) {
				i1 = this.dist[k1];
			}
		}
		
		for(int l1 = 0; l1 < 4; ++l1) {
			this.canSpread[l1] = (this.dist[l1] == i1);
		}
		
		return this.canSpread;
	}
	public int getSlopeDistance(World world, int x, int y, int z, int l, int i1) {
		int j1 = 1000;
		for(int k1 = 0; k1 < 4; ++k1) {
			if(k1 == 0 && i1 == 1 || k1 == 1 && i1 == 0 || k1 == 2 && i1 == 3 || k1 == 3 && i1 == 2) {
				continue;
			}
			int l1 = x;
			int i2 = y;
			int j2 = z;
			switch(k1) {
				case 0:
					--l1;
					break;
				case 1:
					++l1;
					break;
				case 2:
					--j2;
					break;
				case 3:
					++j2;
					break;
			}
			
			if(this.isWaterBlocking(world, l1, i2, j2) || world.getMaterial(l1, i2, j2) == this.material && world.getBlockMetaAt(l1, i2, j2) == 0) {
				continue;
			}
			
			if(!this.isWaterBlocking(world, l1, i2 - 1, j2)) {
				return l;
			}
			if(l >= 4) continue;
			int k2 = this.getSlopeDistance(world, l1, i2, j2, l + 1, k1);
			if(k2 < j1) j1 = k2;
		}
		return j1;
		
	}
	public boolean canSpreadTo(World w, int x, int y, int z) {
		Material m = w.getMaterial(x, y, z);
		if(m == this.material || m == Material.lava) return false;
		return !this.isWaterBlocking(w, x, y, z);
	}
	public boolean isWaterBlocking(World w, int x, int y, int z) {
		int id = w.getBlockIDAt(x, y, z);
		if(id == Block.reeds.blockID) return true;
		else if(id == 0) return false;
		return Block.blocks[id].material.isSolid;
	}
	public void setStatic(World w, int x, int y, int z) {
		int meta = w.getBlockMetaAt(x, y, z);
		w.placeBlock(x, y, z, (byte) (this.blockID + 1), (byte) meta);
	}
	
	public int getHighest(World world, int x, int y, int z, int prev) {
		int depth = this.getDepth(world, x, y, z);
		if(depth < 0) return prev;
		if(depth == 0) this.adjacentLiquidSources++;
		if(depth >= 8) depth = 0;
		
		return prev >= 0 && depth >= prev ? prev : depth;
	}
	
	public int getDepth(World world, int x, int y, int z) {
		if(world.getMaterial(x, y, z) != this.material) return -1;
		return world.getBlockMetaAt(x, y, z);
	}
}
