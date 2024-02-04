package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.World;

public class LadderBlock extends Block{

	public LadderBlock(int id) {
		super(id, Material.decoration);
	}
	
	public boolean isSolidRender() {
		return false;
	}
	
	public void onBlockPlacedByPlayer(World world, int x, int y, int z, int face, Player player) {
		world.placeBlock(x, y, z, (byte) this.blockID, (byte) face);
	}
	
}
