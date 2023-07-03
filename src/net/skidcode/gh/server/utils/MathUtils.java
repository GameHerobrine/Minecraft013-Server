package net.skidcode.gh.server.utils;

public class MathUtils {
	/**
	 * Fast floor
	 */
	public static int ffloor(float f){
		return ((int)f) + (Float.floatToRawIntBits(f) >> 31);
	}
}
