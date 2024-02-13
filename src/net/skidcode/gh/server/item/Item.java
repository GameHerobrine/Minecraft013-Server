package net.skidcode.gh.server.item;

import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.world.World;

public class Item {
	public static Item items[] = new Item[512];
	
	public int itemID;
	
	public Item(int itemID){
		this.itemID = itemID + 256;
		Item.items[this.itemID] = this;
	}
	
	public boolean useOn(ItemInstance item, Player player, World world, int x, int y, int z, int side) {
		return false;
	}
	
}
