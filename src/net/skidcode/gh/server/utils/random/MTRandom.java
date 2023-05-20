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
		int index;
		int newIndex;
		int v8, v9, v10, v5, v6, i;
		index = this.index;
		if (index <= 623) {
			newIndex = index + 1;
		}else {
			if(index == 625) {
				v8 = 5489;
				v9 = 1;
				this.state[0] = 5489;
				this.index = 1;
				v10 = 1; //addr of this->state[1]
				for(i = 2; ; ++i) {
					++v10;
					this.state[v9] = v9 + 0x6C078965 * (v8 ^ (v8 >>> 30));
					v9 = i;
					if(i > 623) {
						break;
					}
					v8 = this.state[v10 - 1];
				}
				this.index = i;
			}
			int arrAddress0to226 = 0;
			int arrAddress227to395 = 0;
			/*
			 *  arrAddress0to226->state[0] = arrAddress0to226->state[397] ^ ((arrAddress0to226->state[1] & 0x7FFFFFFF | arrAddress0to226->state[0] & 0x80000000) >> 1) ^ Random::genrand_int32(void)::mag01[arrAddress0to226->state[1] & 1];
      arrAddress0to226 = (arrAddress0to226 + 4);
			 */
			do {
				this.state[arrAddress0to226] = this.state[arrAddress0to226 + 397] ^ ((this.state[arrAddress0to226 + 1] & 0x7FFFFFFF | this.state[arrAddress0to226] >>> 1) ^ MAG01[this.state[arrAddress0to226 + 1] & 1]);
				++arrAddress0to226;
			}while(arrAddress0to226 != 226);
			/*
			 * arrAddress227to395->state[227] = arrAddress227to395->state[0] ^ ((arrAddress227to395->state[228] & 0x7FFFFFFF | arrAddress227to395->state[0xE3] & 0x80000000) >> 1) ^ Random::genrand_int32(void)::mag01[arrAddress227to395->state[228] & 1];
      arrAddress227to395 = (arrAddress227to395 + 4);
			 */
			do {
				this.state[arrAddress227to395 + 227] = this.state[arrAddress227to395] ^ ((this.state[228 + arrAddress227to395] & 0x7FFFFFFF | this.state[0xE3 + arrAddress227to395] & 0x80000000) >>> 1) ^ MAG01[this.state[arrAddress227to395 + 228] & 1];
				++arrAddress227to395;
			}while(arrAddress227to395 != 395);
			/*
			this->state[623] = this->state[396] ^ ((this->state[0] & 0x7FFFFFFF | this->state[0x26F] & 0x80000000) >> 1) ^ Random::genrand_int32(void)::mag01[this->state[0] & 1];
		    newIndex = 1;
		    index = 0;
		    this->index = 0;
			*/
			
			this.state[623] = this.state[396] ^ ((this.state[0] & 0x7FFFFFFF | this.state[0x26F] & 0x80000000) >>> 1) ^ MTRandom.MAG01[this.state[0] & 1];
			newIndex = 1;
			index = 0;
			this.index = 0;
		}
		v5 = this.state[index];
		/*
		 * this->index = newIndex;
  v6 = ((v5 ^ (v5 >> 0xB)) << 7) & 0x9D2C5680 ^ v5 ^ (v5 >> 0xB);
  return (v6 << 0xF) & 0xEFC60000 ^ v6 ^ (((v6 << 0xF) & 0xEFC60000 ^ v6) >> 0x12);
		 */
		this.index = newIndex;
		v6 = ((v5 ^ (v5 >>> 0xB)) << 7) & 0x9D2C5680 ^ v5 ^ (v5 >>> 0xB);
		return (v6 << 0xF) & 0xEFC60000 ^ v6 ^ (((v6 << 0xF) & 0xEFC60000 ^ v6) >>> 0x12);
    }
    
    public float nextFloat() {
    	return (float)((this.genrand_int32() & 0xffffffffl) * 2.32830644e-10d);
    }
    
    public int nextInt(int a2)
    {
      return (int) ((this.genrand_int32() & 0xffffffffl) % a2);
    }
}
