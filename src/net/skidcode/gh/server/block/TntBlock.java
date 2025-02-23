package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.entity.PrimedTnt;
import net.skidcode.gh.server.world.World;

public class TntBlock extends Block{

	public TntBlock(int id) {
		super(id, Material.explosive);
	}
	
	@Override
	public void destroy(World world, int x, int y, int z, int meta) {
		PrimedTnt tnt = new PrimedTnt(world, x + 0.5f, y + 0.5f, z + 0.5f);
		world.addEntity(tnt);
		//playSound
	}
	@Override
	public void wasExploded(World world, int x, int y, int z)
	{	
		PrimedTnt tnt = new PrimedTnt(world, x + 0.5f, y + 0.5f, z + 0.5f);
		tnt.ticksUntilExplosion = world.random.nextInt(tnt.ticksUntilExplosion / 4) + (tnt.ticksUntilExplosion / 8);
		world.addEntity(tnt);
	}
}
