package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.World;

public class DoorBlock extends Block{

	public DoorBlock(int id, Material m) {
		super(id, m);
		this.setShape(0.5f - 0.5f, 0.0f, 0.5f - 0.5f, 0.5f + 0.5f, 1.0f, 0.5f + 0.5f);
	}
	
	//TODO methods
	
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
	
	public boolean isSolidRender() {
		return false;
	}
}
