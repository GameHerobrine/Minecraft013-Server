package net.skidcode.gh.server.block;

import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Utils;
import net.skidcode.gh.server.world.World;

public class StairBlock extends Block{

	public Block base;	
	
	public StairBlock(int id, Block base) {
		super(id, base.material);
		this.base = base;
		this.setDestroyTime(base.destroyTime);
		this.setExplodeable(base.explosionResistance / 3);
	}
	//TODO more methods
	@Override
	public void setPlacedBy(World world, int x, int y, int z, Player player) {
		switch(Utils.getPlayerDirection(player)) {
			case 0:
				world.placeBlockMetaAndNotifyNearby(x, y, z, (byte) 2);
				break;
			case 1:
				world.placeBlockMetaAndNotifyNearby(x, y, z, (byte) 1);
				break;
			case 2:
				world.placeBlockMetaAndNotifyNearby(x, y, z, (byte) 3);
				break;
			default:
				world.placeBlockMetaAndNotifyNearby(x, y, z, (byte) 0);
				break;
			
		}
	}
	
	@Override
	public boolean isSolidRender() {
		return false;
	}
}
