package net.skidcode.gh.server.utils.random;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

import java.util.Random;

public class BedrockRandom
{

    public BedrockRandom()
    {
        this((new Random()).nextInt());
    }

    public BedrockRandom(int i)
    {
        state = new int[624];
        field_26523_valid = false;
        field_26523_valid = true;
        func_26512__setSeed(i);
    }

    public int func_26520_getSeed()
    {
        return field_26530_seed;
    }

    public void setSeed(long l)
    {
        if(field_26523_valid)
        {
            func_26515_setSeed((int)l);
        }
    }

    public void func_26515_setSeed(int i)
    {
        func_26512__setSeed(i);
    }

    public int nextInt()
    {
        return func_26510__genRandInt32() >>> 1;
    }

    public int nextInt(int i)
    {
        if(i > 0)
        {
            return (int)(Integer.toUnsignedLong(func_26510__genRandInt32()) % (long)i);
        } else
        {
            return 0;
        }
    }

    public int func_26516_nextInt(int i, int j)
    {
        if(i < j)
        {
            return i + nextInt(j - i);
        } else
        {
            return i;
        }
    }

    public int func_26511_nextIntInclusive(int i, int j)
    {
        return func_26516_nextInt(i, j + 1);
    }

    public long func_26509_nextUnsignedInt()
    {
        return Integer.toUnsignedLong(func_26510__genRandInt32());
    }

    public short func_26522_nextUnsignedChar()
    {
        return (short)(func_26510__genRandInt32() & 0xff);
    }

    public boolean nextBoolean()
    {
        return (func_26510__genRandInt32() & 0x8000000) != 0;
    }

    public float nextFloat()
    {
        return (float)func_26518__genRandReal2();
    }

    public float func_26519_nextFloat(float f)
    {
        return nextFloat() * f;
    }

    public float func_26517_nextFloat(float f, float f1)
    {
        return f + nextFloat() * (f1 - f);
    }

    public double nextDouble()
    {
        return func_26518__genRandReal2();
    }

    public double nextGaussian()
    {
        if(field_26525_haveNextNextGaussian)
        {
            field_26525_haveNextNextGaussian = false;
            return (double)field_26532_nextNextGaussian;
        }
        float f;
        float f1;
        float f2;
        do
        {
            f = nextFloat() * 2.0F - 1.0F;
            f1 = nextFloat() * 2.0F - 1.0F;
            f2 = f * f + f1 * f1;
        } while(f2 == 0.0F || f2 > 1.0F);
        float f3 = (float)Math.sqrt((-2F * (float)Math.log(f2)) / f2);
        field_26532_nextNextGaussian = f1 * f3;
        field_26525_haveNextNextGaussian = true;
        return (double)(f * f3);
    }

    public int func_26513_nextGaussianInt(int i)
    {
        return nextInt(i) - nextInt(i);
    }

    public float func_26514_nextGaussianFloat()
    {
        return nextFloat() - nextFloat();
    }

    protected int next(int i)
    {
        return func_26510__genRandInt32() >>> 32 - i;
    }

    private void func_26512__setSeed(int i)
    {
        field_26530_seed = i;
        field_26534_mti = 625;
        field_26525_haveNextNextGaussian = false;
        field_26532_nextNextGaussian = 0.0F;
        func_26508__initGenRandFast(i);
    }

    private void func_26521__initGenRand(int i)
    {
        state[0] = i;
        for(field_26534_mti = 1; field_26534_mti < 624; field_26534_mti++)
        {
            state[field_26534_mti] = 0x6c078965 * (state[field_26534_mti - 1] >>> 30 ^ state[field_26534_mti - 1]) + field_26534_mti;
        }

        indexF = 624;
    }

    private void func_26508__initGenRandFast(int i)
    {
        state[0] = i;
        for(indexF = 1; indexF <= 397; indexF++)
        {
            state[indexF] = 0x6c078965 * (state[indexF - 1] >>> 30 ^ state[indexF - 1]) + indexF;
        }

        field_26534_mti = 624;
    }

    public int func_26510__genRandInt32()
    {
        if(field_26534_mti == 624)
        {
            field_26534_mti = 0;
        } else
        if(field_26534_mti > 624)
        {
            func_26521__initGenRand(5489);
            field_26534_mti = 0;
        }
        if(field_26534_mti >= 227)
        {
            if(field_26534_mti >= 623)
            {
                state[623] = field_26528_MAG_01[state[0] & 1] ^ (state[0] & 0x7fffffff | state[623] & 0x80000000) >>> 1 ^ state[396];
            } else
            {
                state[field_26534_mti] = field_26528_MAG_01[state[field_26534_mti + 1] & 1] ^ (state[field_26534_mti + 1] & 0x7fffffff | state[field_26534_mti] & 0x80000000) >>> 1 ^ state[field_26534_mti - 227];
            }
        } else
        {
            state[field_26534_mti] = field_26528_MAG_01[state[field_26534_mti + 1] & 1] ^ (state[field_26534_mti + 1] & 0x7fffffff | state[field_26534_mti] & 0x80000000) >>> 1 ^ state[field_26534_mti + 397];
            if(indexF < 624)
            {
                state[indexF] = 0x6c078965 * (state[indexF - 1] >>> 30 ^ state[indexF - 1]) + indexF;
                indexF++;
            }
        }
        int i = state[field_26534_mti++];
        i = (i ^ i >>> 11) << 7 & 0x9d2c5680 ^ i ^ i >>> 11;
        i = i << 15 & 0xefc60000 ^ i ^ (i << 15 & 0xefc60000 ^ i) >>> 18;
        return i;
    }

    private double func_26518__genRandReal2()
    {
        return (double)Integer.toUnsignedLong(func_26510__genRandInt32()) * 2.3283064365386963E-010D;
    }

    private static final int field_26531_N = 624;
    private static final int field_26533_M = 397;
    private static final int field_26526_MATRIX_A = 0x9908b0df;
    private static final int field_26529_UPPER_MASK = 0x80000000;
    private static final int field_26535_LOWER_MASK = 0x7fffffff;
    private static final int field_26528_MAG_01[] = {
        0, 0x9908b0df
    };
    private static final double field_26536_TWO_POW_M32 = 2.3283064365386963E-010D;
    private int field_26530_seed;
    private int state[];
    private int field_26534_mti;
    private boolean field_26525_haveNextNextGaussian;
    private float field_26532_nextNextGaussian;
    private int indexF;
    private boolean field_26523_valid;

}
