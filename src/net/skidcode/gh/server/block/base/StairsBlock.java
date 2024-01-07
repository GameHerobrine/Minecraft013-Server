package net.skidcode.gh.server.block.base;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Utils;
import net.skidcode.gh.server.world.World;

public class StairsBlock extends SolidBlock{

	public StairsBlock(int id, Material m) {
		super(id, m);
		this.isSolid = false;
	}
	
	
}
