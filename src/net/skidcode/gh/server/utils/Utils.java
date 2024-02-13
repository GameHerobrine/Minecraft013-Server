package net.skidcode.gh.server.utils;

import net.skidcode.gh.server.player.Player;

public class Utils {
	public static int getPlayerDirection(Player p) {
		return MathUtils.ffloor((p.yaw * 4.0f / 360)+0.5f) & 0x3;
	}
	
	public static int stringHash(String s) {
		int i = 0;
        for(int j = 0; j < s.length(); j++)
        {
            i = i * 31 + s.charAt(j);
        }

        return i;
	}
	
	public static int packBlockPos(int x, int y, int z) {
		return x << 16 | y << 8 | z;
	}
	
}
