package net.skidcode.gh.server.player;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.item.ItemInstance;
import net.skidcode.gh.server.utils.Logger;

public class SurvivalMode extends GameMode{
	
	public long lastDestroyedAt = 0;
	
	
	
	public SurvivalMode(Player player) {
		super(player);
		
	}
	
	public boolean destroyBlock(int x, int y, int z) {
		Block block = Block.blocks[this.player.world.getBlockIDAt(x, y, z)];
		int meta = this.player.world.getBlockMetaAt(x, y, z);
		long currentTime = System.currentTimeMillis();
		
		if(block != null) {
			
			float exceptedTimeMS = 1.0f / (block.getDestroyProgress(this.player) * 16f) / 9.3f * 1000; //TODO more accurate formula if possible
			if(Float.isInfinite(exceptedTimeMS)) {
				Logger.warn(String.format("%s tried to destroy a block that has excepted destruction time of infinity(ID: %d)", this.player.nickname, block.blockID));
				return false;
			}
			
			long exceptedTimeAfter = (long) (this.lastDestroyedAt + (long)exceptedTimeMS);
			if(currentTime < exceptedTimeAfter) {
				Logger.warn(String.format("%s tried to break a block too fast!(ID: %d, Excepted: %d, Resulted: %d)", this.player.nickname, block.blockID, exceptedTimeAfter, currentTime));
				this.lastDestroyedAt = currentTime;
				return false;
			}
		}else {
			return false;
		}
		
		boolean changed = this.player.world.setBlock(x, y, z, 0, 0, 3);
		
		if(changed) {
			block.destroy(this.player.world, x, y, z, meta);
			this.lastDestroyedAt = currentTime;
			
			return true;
		}
		
		return false;
	}
	
	public boolean isCreative() {
		return false;
	}
	
	public boolean isSurvival() {
		return true;
	}
}
