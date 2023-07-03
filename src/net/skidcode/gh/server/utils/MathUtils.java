package net.skidcode.gh.server.utils;

public class MathUtils {
	public static int ffloor(float f){
		return ((int)f) + (Float.floatToRawIntBits(f) >> 31);
	}
	public static long ffloor(double f){
		return ((long)f) + (Double.doubleToRawLongBits(f) >> 63);
	}
	
	public static long fceil(double f){
		return ((long)f) - ~(Double.doubleToRawLongBits(f) >> 63);
	}
	
	public static int fceil(float f){
		return ((int)f) - ~(Float.floatToRawIntBits(f) >> 31);
	}
}
