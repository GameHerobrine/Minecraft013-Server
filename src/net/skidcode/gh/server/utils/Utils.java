package net.skidcode.gh.server.utils;

import net.skidcode.gh.server.player.Player;

public class Utils {
	public static int getPlayerDirection(Player p) {
		return (int)((p.yaw * 4.0 / 360)+0.5) & 0x3;
	}
	
	public static int stringHash(String s) {
		int i = 0;
        for(int j = 0; j < s.length(); j++)
        {
            i = i * 31 + s.charAt(j);
        }

        return i;
	}
}
