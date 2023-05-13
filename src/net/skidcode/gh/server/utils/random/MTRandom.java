package net.skidcode.gh.server.utils.random;

public class MTRandom {
    private static final int N = 624;
    private static final int M = 397;
    private static final int[] MAG01 = {0x0, 0x9908B0DF};
    
    private int[] state;
    private int index;

    public MTRandom(int seed) {
        state = new int[N];
        this.setSeed(seed);
    }

    public void setSeed(int seed) {
    	state[0] = seed;
        for (int i = 1; i < N; ++i) {
            state[i] = 0x6C078965 * (state[i - 1] ^ (state[i - 1] >>> 30)) + i;
        }
        index = N;
	}

	public int genrand_int32() {
        if (index >= N) {
            twistState();
        }
        int maskedNext = state[index++] & 0x7FFFFFFF;
        int mag01Value = MAG01[maskedNext & 1];

        int result = state[(index + M - 1) % N] ^ (maskedNext >>> 1) ^ mag01Value;
        state[index % N] = result;

        result ^= (result >>> 11);
        result ^= (result << 7) & 0x9D2C5680;
        result ^= (result << 15) & 0xEFC60000;
        result ^= (result >>> 18);

        return result;
    }
    
    public float nextFloat() {
    	return this.genrand_int32() * 2.32830644e-10f;
    }
    
    public int nextInt(int a2)
    {
      return this.genrand_int32() % a2;
    }
    private void twistState() {
        for (int i = 0; i < N; ++i) {
            int x = (state[i] & 0x80000000) | (state[(i + 1) % N] & 0x7FFFFFFF);
            state[i] = state[(i + M) % N] ^ (x >>> 1) ^ MAG01[x & 1];
        }
        index = 0;
    }
}
