package net.skidcode.gh.server.utils;

import java.util.Comparator;

import net.skidcode.gh.server.player.Player;

public class ChunkPosSorter implements Comparator<Integer>{
	public Player player;
	public ChunkPosSorter(Player player) {
		this.player = player;
	}
	@Override
	public int compare(Integer o1, Integer o2) {
		int c1 = o1, c2 = o2;
		int c1x = c1 & 0xf, c1z = (c1 & 0xf0) >> 4;
		int c2x = c2 & 0xf, c2z = (c2 & 0xf0) >> 4;
		
		float f = this.player.distanceTo(c1x*16, this.player.posY, c1z*16);
		float f2 = this.player.distanceTo(c2x*16, this.player.posY, c2z*16);
		
		
		return f == f2 ? 0 : (f > f2 ? -1 : 1); //reverse
	}

}
