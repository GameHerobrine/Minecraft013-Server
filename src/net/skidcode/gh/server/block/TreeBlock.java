package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

public class TreeBlock extends Block{

	public TreeBlock(int id) {
		super(id, Material.wood);
		this.unkField_4 = 20;
	}
	
	public void onRemove(World world, int x, int y, int z) {
		if(world.hasChunksAt(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5)) {
			for(int i = -4; i <= 4; ++i) {
				for(int j = -4; j <= 4; ++j) {
					for(int k = -4; k <= 4; ++k) {
						int decayX = i + x;
						int decayY = j + y;
						int decayZ = k + z;
						
						if(world.getBlockIDAt(decayX, decayY, decayZ) == Block.leaves.blockID) {
							int meta = world.getBlockMetaAt(decayX, decayY, decayZ);
							
							if((meta & 4) == 0) {
								world.placeBlockMetaNoUpdate(decayX, decayY, decayZ, (byte) (meta | 4));
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public int getResourceCount(BedrockRandom random) {
		return 1;
	}
	@Override
	public int getSpawnResourcesAuxValue(int meta) {
		return meta;
	}
	
	@Override
	public int getResource(int meta, BedrockRandom random) {
		return Block.log.blockID;
	}
}
