package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.base.DecorationBlock;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.BlockFace;
import net.skidcode.gh.server.world.World;

public class TorchBlock extends DecorationBlock{

	public TorchBlock(int id) {
		super(id, Material.decoration);
		this.name = "Torch";
	}
	public void onBlockPlacedByPlayer(World world, int x, int y, int z, int face, Player player) {
		byte meta2Place = 0;
		switch(face) {
			case BlockFace.DOWN:
				if(Block.blocks[world.getBlockIDAt(x, y - 1, z)].material.isSolid) meta2Place = 5;
				break;
			case BlockFace.NORTH:
				if(Block.blocks[world.getBlockIDAt(x, y, z + 1)].material.isSolid) meta2Place = 4;
				break;
			case BlockFace.SOUTH:
				if(Block.blocks[world.getBlockIDAt(x, y, z - 1)].material.isSolid) meta2Place = 3;
				break;
			case BlockFace.EAST:
				if(Block.blocks[world.getBlockIDAt(x + 1, y, z)].material.isSolid) meta2Place = 2;
				break;
			default:
				if(Block.blocks[world.getBlockIDAt(x - 1, y, z)].material.isSolid) meta2Place = 1;
		}
		world.placeBlock(x, y, z, (byte) this.blockID, meta2Place);
	}
	
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {
		switch(meta) {
			case 5:
				if(!world.isBlockSolid(x, y - 1, z)) this.onBlockRemoved(world, x, y, z);
				break;
			case 4:
				if(!world.isBlockSolid(x, y, z + 1)) this.onBlockRemoved(world, x, y, z);
				break;
			case 3:
				if(!world.isBlockSolid(x, y, z - 1)) this.onBlockRemoved(world, x, y, z); 
				break;
			case 2:
				if(!world.isBlockSolid(x + 1, y, z)) this.onBlockRemoved(world, x, y, z);
				break;
			default:
				if(!world.isBlockSolid(x - 1, y, z)) this.onBlockRemoved(world, x, y, z);
		}
	}
}
