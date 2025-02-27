package net.skidcode.gh.server.world;

import java.util.ArrayList;
import java.util.HashSet;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.entity.Entity;
import net.skidcode.gh.server.utils.AABB;
import net.skidcode.gh.server.utils.BlockPos;
import net.skidcode.gh.server.utils.MathUtils;
import net.skidcode.gh.server.utils.random.BedrockRandom;

public class Explosion {
	public float xCenter, yCenter, zCenter;
	public float power;
	public boolean fire = false;
	public World world;
	public Entity source;
	public BedrockRandom random = new BedrockRandom();
	public HashSet<BlockPos> toDestroy = new HashSet<>();
	
	public Explosion(World world, Entity entity, float x, float y, float z, float power) {
		this.world = world;
		this.source = entity;
		this.xCenter = x;
		this.yCenter = y;
		this.zCenter = z;
		this.power = power;
	}
	
	
	public void explode() {
		for(int xx = 0; xx < 16; ++xx) {
			for(int yy = 0; yy < 16; ++yy) {
				for(int zz = 0; zz < 16; ++zz) {
					if(xx == 0 || xx == 15 || yy == 0 || yy == 15 || zz == 0 || zz == 15) {
						float f = (((float)xx / (16f - 1.0f)) * 2f) - 1.0f;
						float f2 = (((float)yy / (16f - 1.0f)) * 2f) - 1.0f;
						float f3 = (((float)zz / (16f - 1.0f)) * 2f) - 1.0f;
						float sqrt = (float) Math.sqrt(f*f + f2*f2 + f3*f3);
						float f4 = f / sqrt;
						float f5 = f2 / sqrt;
						float f6 = f3 / sqrt;
						float ex = this.power * (0.7f + (this.world.random.nextFloat() * 0.6f)); //yes it uses global random
						float x = this.xCenter;
						float y = this.yCenter;
						float z = this.zCenter;
						while(ex > 0) {
							int xb = MathUtils.ffloor(x);
							int yb = MathUtils.ffloor(y);
							int zb = MathUtils.ffloor(z);
							
							int id = this.world.getBlockIDAt(xb, yb, zb);
							if(id > 0) {
								ex -= (Block.blocks[id].getExplosionResistance(this.source) + 0.3f)  * 0.3f;
							}
							
							if(ex > 0) {
								this.toDestroy.add(new BlockPos(xb, yb, zb)); //TODO maybe pack xyz into integer?						}
							}
							x += f4 * 0.3f;
							y += f5 * 0.3f;
							z += f6 * 0.3f;
							
							ex -= 0.3f * 0.75f;
						}
					}
				}
			}
		}
		float saved = this.power;
		this.power *= 2.0f;
		int minX = MathUtils.ffloor((this.xCenter - this.power) - 1);
		int maxX = MathUtils.ffloor((this.xCenter + this.power) + 1);
		int minY = MathUtils.ffloor((this.yCenter - this.power) - 1);
		int maxY = MathUtils.ffloor((this.yCenter + this.power) + 1);
		int minZ = MathUtils.ffloor((this.zCenter - this.power) - 1);
		int maxZ = MathUtils.ffloor((this.zCenter + this.power) + 1);
		
		ArrayList<Entity> entities = this.world.getEntities(this.source, new AABB(minX, minY, minZ, maxX, maxY, maxZ));
		for(Entity e : entities) {
			float dist = e.distanceTo(this.xCenter, this.yCenter, this.zCenter) / this.power;
			if(dist <= 1) {
				float xd = e.posX - this.xCenter;
				float yd = e.posY - this.yCenter;
				float zd = e.posZ - this.zCenter;
				float di = (float)Math.sqrt(xd*xd + yd*yd + zd*zd);
				xd /= di;
				yd /= di;
				zd /= di;
				float seen = (1 - di) * 1; //TODO getSeenPercent
				//entity.hurt
				e.motionX += xd*seen;
				e.motionY += yd*seen;
				e.motionZ += zd*seen;
			}
		}
		
		//TODO damage entities here
		this.power = saved;
		
		if(this.fire) {
			for(BlockPos bp : this.toDestroy) {
				int id = this.world.getBlockIDAt(bp.x, bp.y, bp.z);
				int idb = this.world.getBlockIDAt(bp.x, bp.y-1, bp.z);
				if(id == 0 && Block.solid[idb] && this.random.nextInt(3) == 0) {
					this.world.setBlock(bp.x, bp.y, bp.z, Block.fire.blockID, 0, 3);
				}
			}
		}
	}
	
	public void addParticles() {
		//plays sound
		for(BlockPos bp : this.toDestroy) {
			int id = this.world.getBlockIDAt(bp.x, bp.y, bp.z);
			//adds smoke and explode particles here
			if(id > 0) {
				//spawn drops Block.blocks[id].spawnResources();
				this.world.setBlock(bp.x, bp.y, bp.z, 0, 0, 3);
				Block.blocks[id].wasExploded(this.world, bp.x, bp.y, bp.z);
			}
		}
	}
	
}
