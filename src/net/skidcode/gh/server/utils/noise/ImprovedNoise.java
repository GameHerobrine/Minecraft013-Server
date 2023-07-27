package net.skidcode.gh.server.utils.noise;


import net.skidcode.gh.server.utils.MathUtils;
import net.skidcode.gh.server.utils.random.BedrockRandom;

public class ImprovedNoise {
	public int[] permutations;
	public float xCoord, yCoord, zCoord;
	
	public static float grad(int hash, float x, float y, float z) {
		int h = hash & 0xF;
		float u = h < 8 ? x : y;
		float v = h < 4 ? y : h == 12 || h == 14 ? x : z;
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
	}
	
	public static float grad2(int i, float d, float d1) {
		int j = i & 0xf;
        float d2 = (1 - ((j & 8) >> 3)) * d;
        float d3 = j >= 4 ? j != 12 && j != 14 ? d1 : d : 0;
        return ((j & 1) != 0 ? -d2 : d2) + ((j & 2) != 0 ? -d3 : d3);
	}
	
	public static float lerp(float t, float a, float b) {
		return a + t * (b - a);
	}
	
	public ImprovedNoise(BedrockRandom r) {
		this.permutations = new int[512];
		
		this.xCoord = r.nextFloat() * 256;
		this.yCoord = r.nextFloat() * 256;
		this.zCoord = r.nextFloat() * 256;
		
		for(int i = 0; i < 256; ++i) {
			this.permutations[i] = i;
		}
		
		for(int i = 0; i < 256; ++i) {
			int k = r.nextInt(256 - i) + i;
			int prev = this.permutations[i];
			this.permutations[i] = this.permutations[k];
			this.permutations[k] = prev;
			this.permutations[i + 256] = this.permutations[i];
		}
	}
	public float getValue(float d, float d1) {
		return this.getValue(d, d1, 0);
	}
	public float getValue(float d, float d1, float d2) {
		float d3 = d + xCoord;
		float d4 = d1 + yCoord;
		float d5 = d2 + zCoord;
		int i = MathUtils.ffloor(d3);
		int j = MathUtils.ffloor(d4);
		int k = MathUtils.ffloor(d5);
		int l = i & 0xff;
		int i1 = j & 0xff;
		int j1 = k & 0xff;
		d3 -= i;
		d4 -= j;
		d5 -= k;
		float d6 = d3 * d3 * d3 * (d3 * (d3 * 6f - 15f) + 10f);
		float d7 = d4 * d4 * d4 * (d4 * (d4 * 6f - 15f) + 10f);
		float d8 = d5 * d5 * d5 * (d5 * (d5 * 6f - 15f) + 10f);
		int k1 = permutations[l] + i1;
		int l1 = permutations[k1] + j1;
		int i2 = permutations[k1 + 1] + j1;
		int j2 = permutations[l + 1] + i1;
		int k2 = permutations[j2] + j1;
		int l2 = permutations[j2 + 1] + j1;
		return lerp(d8, 
				lerp(d7, 
						lerp(d6, grad(permutations[l1], d3, d4, d5), grad(permutations[k2], d3 - 1.0f, d4, d5)), 
						lerp(d6, grad(permutations[i2], d3, d4 - 1.0f, d5), grad(permutations[l2], d3 - 1.0f, d4 - 1.0f, d5))
				), 
				lerp(d7, 
						lerp(d6, grad(permutations[l1 + 1], d3, d4, d5 - 1.0f), grad(permutations[k2 + 1], d3 - 1.0f, d4, d5 - 1.0f)),
						lerp(d6, grad(permutations[i2 + 1], d3, d4 - 1.0f, d5 - 1.0f), grad(permutations[l2 + 1], d3 - 1.0f, d4 - 1.0f, d5 - 1.0f))
				)
		);
	}
	public void add(float ad[], float d, float d1, float d2, 
			int i, int j, int k, float d3, float d4, 
			float d5, float d6)
	{
		if(j == 1)
		{
			int j3 = 0;
			float d12 = 1 / d6;
			for(int i4 = 0; i4 < i; i4++)
			{
				float d14 = (d + (float)i4) * d3 + xCoord;
				int j4 = MathUtils.ffloor(d14);
				int k4 = j4 & 0xff;
				d14 -= j4;
				float d17 = d14 * d14 * d14 * (d14 * (d14 * 6 - 15) + 10);
				for(int l4 = 0; l4 < k; l4++)
				{
					float d19 = (d2 + (float)l4) * d5 + zCoord;
					int j5 = (int)d19;
					if(d19 < (float)j5)
					{
						j5--;
					}
					int l5 = j5 & 0xff;
					d19 -= j5;
					float d21 = d19 * d19 * d19 * (d19 * (d19 * 6 - 15) + 10);
					int l = permutations[k4] + 0;
					int j1 = permutations[l] + l5;
					int k1 = permutations[k4 + 1] + 0;
					int l1 = permutations[k1] + l5;
					float d9 = lerp(d17, grad2(permutations[j1], d14, d19), grad(permutations[l1], d14 - 1, 0, d19));
					float d11 = lerp(d17, grad(permutations[j1 + 1], d14, 0, d19 - 1), grad(permutations[l1 + 1], d14 - 1, 0, d19 - 1));
					float d23 = lerp(d21, d9, d11);
					ad[j3++] += d23 * d12;
				}

			}

			return;
		}
		int i1 = 0;
		float d7 = 1 / d6;
		int i2 = -1;
		float d13 = 0;
		float d15 = 0;
		float d16 = 0;
		float d18 = 0;
		for(int i5 = 0; i5 < i; i5++)
		{
			float d20 = (d + (float)i5) * d3 + xCoord;
			int k5 = MathUtils.ffloor(d20);
			int i6 = k5 & 0xff;
			d20 -= k5;
			float d22 = d20 * d20 * d20 * (d20 * (d20 * 6f - 15f) + 10f);
			for(int j6 = 0; j6 < k; j6++)
			{
				float d24 = (d2 + (float)j6) * d5 + zCoord;
				int k6 = MathUtils.ffloor(d24);
				int l6 = k6 & 0xff;
				d24 -= k6;
				float d25 = d24 * d24 * d24 * (d24 * (d24 * 6f - 15f) + 10f);
				for(int i7 = 0; i7 < j; i7++)
				{
					float d26 = (d1 + (float)i7) * d4 + yCoord;
					int j7 = MathUtils.ffloor(d26);
					int k7 = j7 & 0xff;
					d26 -= j7;
					float d27 = d26 * d26 * d26 * (d26 * (d26 * 6f - 15f) + 10f);
					if(i7 == 0 || k7 != i2)
					{
						i2 = k7;
						int j2 = permutations[i6] + k7;
						int k2 = permutations[j2] + l6;
						int l2 = permutations[j2 + 1] + l6;
						int i3 = permutations[i6 + 1] + k7;
						int k3 = permutations[i3] + l6;
						int l3 = permutations[i3 + 1] + l6;
						d13 = lerp(d22, grad(permutations[k2], d20, d26, d24), grad(permutations[k3], d20 - 1, d26, d24));
						d15 = lerp(d22, grad(permutations[l2], d20, d26 - 1, d24), grad(permutations[l3], d20 - 1, d26 - 1, d24));
						d16 = lerp(d22, grad(permutations[k2 + 1], d20, d26, d24 - 1), grad(permutations[k3 + 1], d20 - 1, d26, d24 - 1));
						d18 = lerp(d22, grad(permutations[l2 + 1], d20, d26 - 1, d24 - 1), grad(permutations[l3 + 1], d20 - 1, d26 - 1, d24 - 1));
					}
					float d28 = lerp(d27, d13, d15);
					float d29 = lerp(d27, d16, d18);
					float d30 = lerp(d25, d28, d29);
					ad[i1++] += d30 * d7;
				}

			}

		}
	}
	
	
}
