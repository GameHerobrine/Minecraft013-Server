package net.skidcode.gh.server.player;

import net.skidcode.gh.server.block.Block;

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
