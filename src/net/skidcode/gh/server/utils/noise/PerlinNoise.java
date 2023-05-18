package net.skidcode.gh.server.utils.noise;

import java.util.Arrays;
import java.util.Random;

import net.skidcode.gh.server.utils.random.MTRandom;

public class PerlinNoise {
	public ImprovedNoise[] noises;
	public int octavesAmount;
	public PerlinNoise(MTRandom r, int octaves) {
		this.octavesAmount = octaves;
		this.noises = new ImprovedNoise[octaves];
		for(int i = 0; i < octaves; ++i) {
			this.noises[i] = new ImprovedNoise(r);
		}
	}
	
	public float getValue(float x, float y) {
		float noise = 0;
		float scale = 1;

		for (int i = 0; i < this.octavesAmount; i++) {
			noise += this.noises[i].getValue(x * scale, y * scale) / scale;
			scale /= 2.0;
		}

		return noise;
	}
	public float[] getRegion(float ad[], int i, int j, int k, int l, float d, float d1, float d2)
    {
        return getRegion(ad, i, 10, j, k, 1, l, d, 1, d1);
    }
	public float[] getRegion(float[] noiseArray, float x, float y, float z, int width, int height, int depth, float scaleX, float scaleY, float scaleZ) {
		if (noiseArray == null) {
			noiseArray = new float[width * height * depth];
		} else {
			Arrays.fill(noiseArray, 0);
		}

		float scale = 1;

		for (int i = 0; i < this.octavesAmount; i++) {
			this.noises[i].add(noiseArray, x, y, z, width, height, depth, scaleX * scale, scaleY * scale, scaleZ * scale, scale);
			scale /= 2.0;
		}

		return noiseArray;
	}
}
