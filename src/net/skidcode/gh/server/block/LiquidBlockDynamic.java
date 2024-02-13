package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class LiquidBlockDynamic extends LiquidBlock{
	
	public int something = 0;
	public boolean[] boolArr = new boolean[4];
	public int[] intArr = new int[4];
	
	public LiquidBlockDynamic(int id, Material m) {
		super(id, m);
		//*((_DWORD *)this + 0x1B) = 0;
		//TODO unknown property
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
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
                    func_292_i(world, i, j, k);
                }*/ //TODO check
				if(this.material != Material.lava) {
					//TODO in case of further updates Block.blocks[i1].drop();
				}
			}
			w.placeBlockAndNotifyNearby(x, y, z, (byte) this.blockID, (byte) meta);
		}
	}
	public boolean[] getSpread(World w, int x, int y, int z) {
		for(int l = 0; l < 4; ++l) {
			this.intArr[l] = 1000;
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
				this.intArr[l] = 0;
			}else {
				this.intArr[l] = this.getSlopeDistance(w, j1, i2, j2, 1, l);
			}
		}
		
		int i1 = this.intArr[0];
		for(int k1 = 1; k1 < 4; ++k1) {
			if(this.intArr[k1] < i1) {
				i1 = this.intArr[k1];
			}
		}
		
		for(int l1 = 0; l1 < 4; ++l1) {
			this.boolArr[l1] = (this.intArr[l1] == i1);
		}
		
		return this.boolArr;
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
	
	public int getMetaForSelfMaterial(World world, int x, int y, int z) {
		return world.getMaterial(x, y, z) != this.material ? -1 : world.getBlockMetaAt(x, y, z);
	}
	public int weirdfunc1(World world, int x, int y, int z, int u) {
		int metaself = this.getMetaForSelfMaterial(world, x, y, z);
		if(metaself < 0) return u;
		if(metaself == 0) this.something++;
		if(metaself >= 8) metaself = 0;
		
		return u >= 0 && metaself >= u ? u : metaself;
	}
	
	public int getDepth(World world, int x, int y, int z) {
		if(world.getMaterial(x, y, z) != this.material) return -1;
		return world.getBlockMetaAt(x, y, z);
	}
}
