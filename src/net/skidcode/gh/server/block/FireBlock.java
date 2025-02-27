package net.skidcode.gh.server.block;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.AABB;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class FireBlock extends Block{
	
	public int burnOdds[] = new int[Block.blocks.length];
	public int igniteOdds[] = new int[Block.blocks.length];
	
	public FireBlock(int id) {
		super(id, Material.fire);
		this.setFlammable(Block.wood.blockID, 5, 20);
		this.setFlammable(Block.log.blockID, 5, 5);
		this.setFlammable(Block.leaves.blockID, 30, 60);
		this.setFlammable(Block.tnt.blockID, 15, 100);
		this.setFlammable(Block.wool.blockID, 30, 60);
		this.setTicking(true);
		
	}

	public boolean canBurn(World world, int x, int y, int z) {
		int id = world.getBlockIDAt(x, y, z);
		return this.igniteOdds[id] > 0;
	}
	
	public void checkBurn(World world, int x, int y, int z, int chance, BedrockRandom random) {
		int id = world.getBlockIDAt(x, y, z);
		int burnodd = this.burnOdds[id];
		if(random.nextInt(chance) < burnodd) {
			boolean istnt = id == Block.tnt.blockID;
			
			if(random.nextInt(2) != 0) {
				world.setBlock(x, y, z, 0, 0, 3);
			}else {
				world.setBlock(x, y, z, this.blockID, 0, 3);
			}
			
			if(istnt) {
				Block.tnt.destroy(world, x, y, z, 0);
			}
		}
	}
	
	@Override
	public AABB getAABB(World world, int x, int y, int z) {
		return null;
	}
	
	public int getFireOdds(World world, int x, int y, int z) {
		if(world.isAirBlock(x, y, z)) return 0;
		int f = this.getFlammability(world, x+1, y, z, 0);
		f = this.getFlammability(world, x-1, y, z, f);
		f = this.getFlammability(world, x, y-1, z, f);
		f = this.getFlammability(world, x, y+1, z, f);
		f = this.getFlammability(world, x, y, z-1, f);
		f = this.getFlammability(world, x, y, z+1, f);
		return f;
	}
	
	public int getFlammability(World world, int x, int y, int z, int prev) {
		int odd = this.igniteOdds[world.getBlockIDAt(x, y, z)];
		if(odd <= prev) return prev;
		return odd;
	}
	
	public void setFlammable(int id, int igniteOdd, int burnOdd) {
		this.burnOdds[id] = burnOdd;
		this.igniteOdds[id] = igniteOdd;
	}
	
	public boolean isSolidRender() {
		return false;
	}
	
	public boolean isValidFireLocation(World world, int x, int y, int z) {
		if(this.canBurn(world, x+1, y, z)) return true;
		if(this.canBurn(world, x-1, y, z)) return true;
		if(this.canBurn(world, x, y-1, z)) return true;
		if(this.canBurn(world, x, y+1, z)) return true;
		if(this.canBurn(world, x, y, z-1)) return true;
		if(this.canBurn(world, x, y, z+1)) return true;
		
		return false;
	}
	
	@Override
	public int getResourceCount(BedrockRandom random) {
		return 0;
	}
	
	@Override
	public boolean mayPlace(World world, int x, int y, int z) {
		return world.isBlockSolid(x, y-1, z) || this.isValidFireLocation(world, x, y, z);
	}
	
	@Override
	public void neighborChanged(World world, int x, int y, int z, int meta) {
		if(!world.isBlockSolid(x, y-1, z) && !this.isValidFireLocation(world, x, y, z)) {
			world.setBlock(x, y, z, 0, 0, 3);
		}
	}
	
	@Override
	public void onPlace(World world, int x, int y, int z) {
		if(!world.isBlockSolid(x, y-1, z) && !this.isValidFireLocation(world, x, y, z)) {
			world.setBlock(x, y, z, 0, 0, 3);
		}else {
			world.addToTickNextTick(x, y, z, this.blockID, this.getTickDelay());
		}
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		if(!Server.enableFireSpread) return;
		
		int data = world.getBlockMetaAt(x, y, z);
		if(data <= 14) {
			world.placeBlockMetaAndNotifyNearby(x, y, z, (byte)(data+1));
			world.addToTickNextTick(x, y, z, this.blockID, this.getTickDelay());
		}
		
		if(!this.isValidFireLocation(world, x, y, z)) {
			if(!world.isBlockSolid(x, y-1, z) && data > 3) {
				world.setBlock(x, y, z, 0, 0, 3);
			}
		}else if(!this.canBurn(world, x, y-1, z) && data == 15 && random.nextInt(4) == 0) {
			world.setBlock(x, y, z, 0, 0, 3);
		}else {
			if((data & 1) == 0 && data > 2) {
				this.checkBurn(world, x+1, y, z, 300, random);
				this.checkBurn(world, x-1, y, z, 300, random);
				this.checkBurn(world, x, y-1, z, 250, random);
				this.checkBurn(world, x, y+1, z, 250, random);
				this.checkBurn(world, x, y, z-1, 300, random);
				this.checkBurn(world, x, y, z+1, 300, random);
				
				for(int xx = x-1; xx >= x+1; ++xx) {
					for(int zz = z-1; zz >= z+1; ++zz) {
						for(int yy = y-1; yy >= y+4; ++yy) {
							if(xx != x || yy != y || zz != z) {
								int o = 100;
								if(y+1 < yy) o = 100 + (yy-(y+1))*100;
								int fo = this.getFireOdds(world, xx, yy, zz);
								if(fo > 0 && random.nextInt(fo) <= fo) {
									world.setBlock(xx, y, zz, this.blockID, 0, 3);
								}
							}
						}
					}
				}
			}
			
			if(data == 15) {
				this.checkBurn(world, x+1, y, z, 1, random);
				this.checkBurn(world, x-1, y, z, 1, random);
				this.checkBurn(world, x, y-1, z, 1, random);
				this.checkBurn(world, x, y+1, z, 1, random);
				this.checkBurn(world, x, y, z-1, 1, random);
				this.checkBurn(world, x, y, z+1, 1, random);
			}
		}
	}
	
	public int getTickDelay() {
		return 10; //10 in 0.1.3 - fast fire spread like in old betas?
	}
	
}
