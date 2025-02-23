package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class RedstoneOreBlock extends Block{
	public boolean lit;
	public RedstoneOreBlock(int id, boolean lit) {
		super(id, Material.stone);
		this.setTicking(lit);
		this.lit = lit;
	}
	
	@Override
	public int getTickDelay() {
		return 30;
	}
	
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		if(this.blockID == Block.glowingRedstoneOre.blockID) {
			world.setBlock(x, y, z, Block.redstoneOre.blockID, 0, 3);
		}
	}
	
	//TODO methods
}
