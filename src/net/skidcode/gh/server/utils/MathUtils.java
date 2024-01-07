package net.skidcode.gh.server.utils;

public class MathUtils {
	
	public static float sin(float f)
    {
        return SIN_TABLE[(int)(f * 10430f) & 0xffff];
    }

    public static float cos(float f)
    {
        return SIN_TABLE[(int)(f * 10430f + 16384F) & 0xffff];
    }

	public static int ffloor(float f){
		return ((int)f) + (Float.floatToRawIntBits(f - (int)f) >> 31);
	}
	public static long ffloor(double f){
		return ((long)f) + (Double.doubleToRawLongBits(f - (long)f) >> 63);
	}
	
	public static long fceil(double f){
		return ((long)f) - (Double.doubleToRawLongBits(((long)f)-f) >> 63);
	}
	
	public static int fceil(float f){
		return ((int)f) - (Float.floatToRawIntBits(((int)f)-f) >> 31);
	}
	
	public static final float SIN_TABLE[];
	static 
	{
		SIN_TABLE = new float[0x10000];
		for(int i = 0; i < 0x10000; i++)
		{
			SIN_TABLE[i] = (float)Math.sin(i / 10430.0f);
		}
	}
	
}
