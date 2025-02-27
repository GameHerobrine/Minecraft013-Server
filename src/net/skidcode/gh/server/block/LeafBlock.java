package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class LeafBlock extends TransparentBlock{
	
	public LeafBlock(int id) {
		super(id, Material.leaves, false);
		this.unkField_70 = 0;
		this.setTicking(true);
	}
	@Override
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		//TODO tick
	}
	
	@Override
	public int getResourceCount(BedrockRandom random) {
		return random.nextInt(16) == 0 ? 1 : 0;
	}
	
	@Override
	public boolean isSolidRender() {
		return false;
	}
	
	@Override
	public void onRemove(World world, int x, int y, int z) {
		if(world.hasChunksAt(x - 2, y - 2, z - 2, x + 2, y + 2, z + 2)) {
			for(int i = -1; i <= 1; ++i) {
				for(int j = -1; j <= 1; ++j) {
					for(int k = -1; k <= 1; ++k) {
						if(world.getBlockIDAt(x + i, y + j, z + k) == Block.leaves.blockID) {
							int meta = world.getBlockMetaAt(x + i, y + j, z + k);
							world.placeBlockMetaNoUpdate(x + i, y + j, z + k, (byte) (meta | 4));
						}
					}
				}
			}
		}
	}
	
	@Override
	public int getResource(int meta, BedrockRandom random) {
		return 0;
	}
}
