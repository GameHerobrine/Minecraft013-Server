package net.skidcode.gh.server.block.base;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Utils;
import net.skidcode.gh.server.world.World;

public class StairsBlock extends SolidBlock{

	public StairsBlock(int id, Material m) {
		super(id, m);
	}
	
	public void onBlockPlacedByPlayer(World world, int x, int y, int z, int face, Player player) {
		switch(Utils.getPlayerDirection(player)) {
			case 0:
				world.placeBlock(x, y, z, (byte) this.blockID, (byte) 2);
				break;
			case 1:
				world.placeBlock(x, y, z, (byte) this.blockID, (byte) 1);
				break;
			case 2:
				world.placeBlock(x, y, z, (byte) this.blockID, (byte) 3);
				break;
			default:
				world.placeBlock(x, y, z, (byte) this.blockID, (byte) 0);
				break;
			
		}
		
	}
}
