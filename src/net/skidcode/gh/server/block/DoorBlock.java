package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.AABB;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class DoorBlock extends Block{

	public DoorBlock(int id, Material m) {
		super(id, m);
		this.setShape(0.5f - 0.5f, 0.0f, 0.5f - 0.5f, 0.5f + 0.5f, 1.0f, 0.5f + 0.5f);
	}
	
	//TODO methods
	
	public static int getDir(int data) {
		if((data & 4) != 0) return data & 3;
		return (data - 1) & 3;
	}
	
	@Override
	public void updateShape(World world, int x, int y, int z) {
		int data = world.getBlockMetaAt(x, y, z);
		int dir = DoorBlock.getDir(data);
		this.setShape(dir);
	}
	
	public void setShape(int dir) {
		if(dir == 0) this.setShape(0, 0, 0, 1, 1, 0.1875f);
		else if(dir == 1) this.setShape(1 - 0.1875f, 0, 0, 1, 1, 1);
		else if(dir == 2) this.setShape(1, 0, 1 - 0.1875f, 1, 1, 1);
		else if(dir == 3) this.setShape(0, 0, 0, 0.1875f, 1, 1);
		else this.setShape(0, 0, 0, 1, 2, 1);
	}
	
	@Override
	public AABB getAABB(World world, int x, int y, int z) {
		this.updateShape(world, x, y, z);
		return super.getAABB(world, x, y, z);
	}
	
	@Override
	public boolean use(World world, int x, int y, int z, Player player) {
		if(this.material == Material.metal) return true; 
		int metadata = world.getBlockMetaAt(x, y, z);
		
		if((metadata & 8) == 1) {
			if(world.getBlockIDAt(x, y - 1, z) == this.blockID) {
				this.use(world, x, y - 1, z, player);
			}
		}else {
			metadata ^= 4;
			
			if(world.getBlockIDAt(x, y + 1, z) == this.blockID) {
				world.placeBlockMetaAndNotifyNearby(x, y + 1, z, (byte) (metadata + 8));
			}
			
			world.placeBlockMetaAndNotifyNearby(x, y, z, (byte) (metadata));
		}
		return true;
	}
	
	@Override
	public boolean mayPlace(World world, int x, int y, int z) {
		if(y > 126) return false;
		if(world.isBlockSolid(x, y-1, z)) {
			if(this.mayPlace(world, x, y, z) && this.mayPlace(world, x, y+1, z)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isSolidRender() {
		return false;
	}
	
	@Override
	public int getResource(int meta, BedrockRandom random) {
		if((meta & 8) != 0) return 0;
		if(this.material == Material.metal) return 0; //TODO Item.door_iron items
		return 0; //TODO Item.door_wood items
	}
}
