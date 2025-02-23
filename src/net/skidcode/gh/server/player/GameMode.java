package net.skidcode.gh.server.player;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.item.ItemInstance;
import net.skidcode.gh.server.utils.Logger;

public class GameMode {
	
	public Player player;
	
	public GameMode(Player player) {
		this.player = player;
	}
	
	public boolean isCreative() {
		return true;
	}
	
	public boolean isSurvival() {
		return false;
	}
	
	public float getPickRange() {
		return 7.5f;
	}
	
	public boolean useItemOn(ItemInstance item, int x, int y, int z, int face) {
		if(item != null && item.getItem() == null) {
			Logger.warn(String.format("%s tried using null item(ID %d Meta %d Count %d)!", player.nickname, item.itemID, item.itemMeta, item.itemCount));
			return false;
		}
		int blockID = this.player.world.getBlockIDAt(x, y, z);
		
		if(Block.blocks[blockID] != null && Block.blocks[blockID].use(this.player.world, x, y, z, this.player)) {
			return true;
		}
		
		if(item != null) {
			return item.useOn(this.player, this.player.world, x, y, z, face);
		}
		
		return false;
	}
	
	public boolean destroyBlock(int x, int y, int z) {
		Block block = Block.blocks[this.player.world.getBlockIDAt(x, y, z)];
		int meta = this.player.world.getBlockMetaAt(x, y, z);
		
		boolean changed = this.player.world.setBlock(x, y, z, 0, 0, 3);
		
		if(changed) {
			block.destroy(this.player.world, x, y, z, meta);
			return true;
		}
		
		return false;
	}
}
