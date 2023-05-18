package net.skidcode.gh.server.utils.random;

public class MTRandom {
    private static final int N = 624;
    private static final int M = 397;
    private static final int[] MAG01 = {0x0, 0x9908B0DF};
    
    private int[] state;
    private int index = 0;
    public MTRandom() {}
    public MTRandom(int seed) {
        state = new int[N];
        this.setSeed(seed);
    }

    public void setSeed(int seed) {
    	this.state[0] = seed & 0xFFFFFFFF;
        for (this.index = 1; this.index < N; ++this.index) {
            this.state[this.index] = (1812433253 * (this.state[this.index-1] ^ (this.state[this.index-1] >>> 30)) + this.index);
        }
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
    	return (float)((this.genrand_int32() & 0xffffffffl) * 2.32830644e-10d);
    }
    
    public int nextInt(int a2)
    {
      return (int) ((this.genrand_int32() & 0xffffffffl) % a2);
    }
    private void twistState() {
    	int kk, y;
        
        for (kk = 0; kk < N - M; ++kk) {
            y = (this.state[kk] & 0x80000000) | (this.state[kk + 1] & 0x7fffffff);
            this.state[kk] = this.state[kk + M] ^ (y >>> 1) ^ MAG01[y & 0x1];
        }
        
        for (; kk < N - 1; ++kk) {
            y = (this.state[kk] & 0x80000000) | (this.state[kk + 1] & 0x7fffffff);
            this.state[kk] = this.state[kk + (M - N)] ^ (y >>> 1) ^ MAG01[y & 0x1];
        }
        
        y = (this.state[N - 1] & 0x80000000) | (this.state[0] & 0x7fffffff);
        this.state[N - 1] = this.state[M - 1] ^ (y >>> 1) ^ MAG01[y & 0x1];
        
        this.index = 0;
    }
}
