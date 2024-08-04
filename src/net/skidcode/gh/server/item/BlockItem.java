package net.skidcode.gh.server.item;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.World;

public class BlockItem extends Item{
	public int blockID;
	
	public BlockItem(int id) {
		super(id);
		this.blockID = id+256;
	}
	
	public boolean useOn(ItemInstance item, Player player, World world, int x, int y, int z, int side) {
		if(world.getBlockIDAt(x, y, z) == Block.snowLayer.blockID) {
			side = 0;
		}else {
			switch(side) {
				case 0:
					--y;
					break;
				case 1:
					++y;
					break;
				case 2:
					--z;
					break;
				case 3:
					++z;
					break;
				case 4:
					--x;
					break;
				case 5:
					++x;
					break;
			}
		}
		
		if(item.itemCount == 0) return false;
		if(!world.mayPlace(this.blockID, x, y, z, false)) {
			return false;
		}
		
		Block b = Block.blocks[this.blockID];
		if(!world.setBlock(x, y, z, this.blockID, 0, 3)) {
			return true;
		}
		
		b.setPlacedOnFace(world, x, y, z, side);
		b.setPlacedBy(world, x, y, z, player);
		
		return true;
	}
}
