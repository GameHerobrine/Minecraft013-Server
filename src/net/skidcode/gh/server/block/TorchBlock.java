package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.AABB;
import net.skidcode.gh.server.utils.BlockFace;
import net.skidcode.gh.server.world.World;

public class TorchBlock extends Block{

	public TorchBlock(int id) {
		super(id, Material.decoration);
		this.setTicking(true); //TODO ticking might be used only for clientside rendering
	}
	
	public boolean checkCanSurvive(World world, int x, int y, int z){
		if(this.mayPlace(world, x, y, z)) return true;
		world.setBlock(x, y, z, 0, 0, 3);
		return false;
	}
	
	@Override
	public AABB getAABB(World world, int x, int y, int z) {
		return null;
	}
	
	@Override
	public boolean mayPlace(World world, int x, int y, int z) {
		
		if(world.isBlockSolid(x - 1, y, z) || world.isBlockSolid(x + 1, y, z)) return true;
		if(world.isBlockSolid(x, y, z - 1) || world.isBlockSolid(x, y, z + 1)) return true;
		
		return world.isBlockSolid(x, y - 1, z);
	}
	
	@Override
	public void setPlacedOnFace(World world, int x, int y, int z, int face) {
		byte meta2Place = 0;
		switch(face) {
			case BlockFace.DOWN:
				if(world.isBlockSolid(x, y - 1, z)) meta2Place = 5;
				break;
			case BlockFace.NORTH:
				if(world.isBlockSolid(x, y, z + 1)) meta2Place = 4;
				break;
			case BlockFace.SOUTH:
				if(world.isBlockSolid(x, y, z - 1)) meta2Place = 3;
				break;
			case BlockFace.EAST:
				if(world.isBlockSolid(x + 1, y, z)) meta2Place = 2;
				break;
			default:
				if(world.isBlockSolid(x - 1, y, z)) meta2Place = 1;
		}
		world.placeBlock(x, y, z, (byte) this.blockID, meta2Place);
	}
	
	@Override
	public void neighborChanged(World world, int x, int y, int z, int meta) {
		boolean survive = this.checkCanSurvive(world, x, y, z);
		if(survive) {
			int metaHere = world.getBlockMetaAt(x, y, z);
			boolean shouldDestroy = false;
			
			if(!world.isBlockSolid(x - 1, y, z) && metaHere == 1) shouldDestroy = true;
			if(!world.isBlockSolid(x + 1, y, z) && metaHere == 2) shouldDestroy = true;
			if(!world.isBlockSolid(x, y, z - 1) && metaHere == 3) shouldDestroy = true;
			if(!world.isBlockSolid(x, y, z + 1) && metaHere == 4) shouldDestroy = true;
			if(!world.isBlockSolid(x, y - 1, z) && metaHere == 5) shouldDestroy = true;

			if(shouldDestroy) {
				world.setBlock(x, y, z, 0, 0, 3);
			}
		}
	}
	
	@Override
	public boolean isSolidRender() {
		return false;
	}
}
