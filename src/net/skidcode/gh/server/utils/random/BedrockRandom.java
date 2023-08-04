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
        valid = false;
        valid = true;
        func_26512__setSeed(i);
    }

    public int func_26520_getSeed()
    {
        return seed;
    }

    public void setSeed(long l)
    {
        if(valid)
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
            return (int)((((long)func_26510__genRandInt32()) & 0xffffffffl) % (long)i);
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
        return Integer.toUnsignedLong(func_26510__genRandInt32()) * 2.32830644e-10f;
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
        if(haveNextNextGaussian)
        {
            haveNextNextGaussian = false;
            return (double)nextNextGaussian;
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
        nextNextGaussian = f1 * f3;
        haveNextNextGaussian = true;
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

    public void func_26512__setSeed(int i)
    {
        seed = i;
        mti = 625;
        haveNextNextGaussian = false;
        nextNextGaussian = 0.0F;
        func_26508__initGenRandFast(i);
    }

    public void func_26521__initGenRand(int i)
    {
        state[0] = i;
        mti = 1;
        do{
            state[mti] = 0x6c078965 * (state[mti - 1] >>> 30 ^ state[mti - 1]) + mti;
        }while(++mti < 624);

        indexF = 624;
    }

    public void func_26508__initGenRandFast(int i)
    {
        state[0] = i;
        indexF = 1;
        do{
            state[indexF] = 0x6c078965 * (state[indexF - 1] >>> 30 ^ state[indexF - 1]) + indexF;
        }while(++indexF <= 397);
        mti = 624;
    }

    public int func_26510__genRandInt32()
    {
        if(mti == 624)
        {
            mti = 0;
        } else
        if(mti > 624)
        {
            func_26521__initGenRand(5489);
            mti = 0;
        }
        if(mti >= 227)
        {
            if(mti >= 623)
            {
                state[623] = MAG_01[state[0] & 1] ^ (state[0] & 0x7fffffff | state[623] & 0x80000000) >>> 1 ^ state[396];
            } else
            {
                state[mti] = MAG_01[state[mti + 1] & 1] ^ (state[mti + 1] & 0x7fffffff | state[mti] & 0x80000000) >>> 1 ^ state[mti - 227];
            }
        } else
        {
            state[mti] = MAG_01[state[mti + 1] & 1] ^ (state[mti + 1] & 0x7fffffff | state[mti] & 0x80000000) >>> 1 ^ state[mti + 397];
            if(indexF < 624)
            {
                state[indexF] = 0x6c078965 * (state[indexF - 1] >>> 30 ^ state[indexF - 1]) + indexF;
                indexF++;
            }
        }
        int i = state[mti++];
        i = (i ^ i >>> 11) << 7 & 0x9d2c5680 ^ i ^ i >>> 11;
        i = i << 15 & 0xefc60000 ^ i ^ (i << 15 & 0xefc60000 ^ i) >>> 18;
        return i;
    }

    public double func_26518__genRandReal2()
    {
        return (double)Integer.toUnsignedLong(func_26510__genRandInt32()) * 2.3283064365386963E-010D;
    }

    public static final int MAG_01[] = {
        0, 0x9908b0df
    };
    public int seed;
    public int state[];
    public int mti;
    public boolean haveNextNextGaussian;
    public float nextNextGaussian;
    public int indexF;
    public boolean valid;

}
