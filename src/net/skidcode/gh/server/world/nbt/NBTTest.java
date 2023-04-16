package net.skidcode.gh.server.world.nbt;

import java.io.IOException;
import net.skidcode.gh.server.world.parser.vanilla.VanillaParser;

public class NBTTest {
	
	public static void main(String args[]) {
		try {
			VanillaParser.parseVanillaWorld();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
