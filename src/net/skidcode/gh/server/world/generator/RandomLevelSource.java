package net.skidcode.gh.server.world.generator;

import java.util.Random;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.noise.PerlinNoise;
import net.skidcode.gh.server.utils.random.MTRandom;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.biome.Biome;
import net.skidcode.gh.server.world.chunk.Chunk;

public class RandomLevelSource implements LevelSource{ //TODO all public?, try to make more vanilla
	private World world;
	private MTRandom rand;
	private PerlinNoise upperInterpolationNoise;
	private PerlinNoise lowerInterpolationNoise;
	private PerlinNoise interpolationNoise;
	private PerlinNoise beachNoise;
	private PerlinNoise surfaceDepthNoise;
	private PerlinNoise biomeNoise;
	private PerlinNoise depthNoise;
	private PerlinNoise treeNoise;
	private float[] biomeNoises;
	private float[] depthNoises;
	private float[] interpolationNoises;
	private float[] upperInterpolationNoises;
	private float[] lowerInterpolationNoises;
	private Biome[] biomes;
	private float[] heights;
	private float[] sandNoises;
	private float[] gravelNoises;
	private float[] surfaceDepthNoises;

	public RandomLevelSource(World world, int seed) {
		this.world = world;
		this.rand = new MTRandom(seed);
		this.upperInterpolationNoise = new PerlinNoise(this.rand, 16);
		this.lowerInterpolationNoise = new PerlinNoise(this.rand, 16);
		this.interpolationNoise = new PerlinNoise(this.rand, 8);
		this.beachNoise = new PerlinNoise(this.rand, 4);
		this.surfaceDepthNoise = new PerlinNoise(this.rand, 4);
		this.biomeNoise = new PerlinNoise(this.rand, 10);
		this.depthNoise = new PerlinNoise(this.rand, 16);
		this.treeNoise = new PerlinNoise(this.rand, 8);
	}

	@Override
	public float[] getHeights(float[] heights, int chunkX, int chunkY, int chunkZ, int scaleX, int scaleY, int scaleZ) {
		
		if(heights == null) heights = new float[scaleX * scaleY * scaleZ];
		float[] rainNoises = this.world.biomeSource.rainfallNoises;
		float[] tempNoises = this.world.biomeSource.temperatureNoises;
		this.biomeNoises = this.biomeNoise.getRegion(null, chunkX, chunkZ, scaleX, scaleZ, 1.121f, 1.121f, 0.5f);
		this.depthNoises = this.depthNoise.getRegion(null, chunkX, chunkZ, scaleX, scaleZ, 200.0f, 200.0f, 0.5f);
		this.interpolationNoises = this.interpolationNoise.getRegion(null, chunkX, chunkY, chunkZ, scaleX, scaleY, scaleZ, 8.5552f, 4.2776f, 8.5552f);
		this.upperInterpolationNoises = this.upperInterpolationNoise.getRegion(null, chunkX, chunkY, chunkZ, scaleX, scaleY, scaleZ, 684.41f, 684.41f, 684.41f);
		this.lowerInterpolationNoises = this.lowerInterpolationNoise.getRegion(null, chunkX, chunkY, chunkZ, scaleX, scaleY, scaleZ, 684.41f, 684.41f, 684.41f);
		int index = 0;
		int rainbiomedepthIndex = 0;
		int i = 16 / scaleX;
		
		float f, f2;
		for(int blockX = 0; blockX < scaleX; ++blockX) {
			int i2 = (blockX * i) + (i / 2);
			for(int blockZ = 0; blockZ < scaleZ; ++blockZ) {
				int i3 = (blockZ * i) + (i / 2);
				float f3 = 1.0f - (rainNoises[(i2 * 16) + i3] * tempNoises[(i2 * 16) + i3]);
				float f4 = f3 * f3;
				float f5 = ((this.biomeNoises[rainbiomedepthIndex] + 256.0f) / 512.0f) * (1.0f - (f4 * f4));
				
				if(f5 > 1) f5 = 1;
				
				float f6 = this.depthNoises[rainbiomedepthIndex++] / 8000;
				if(f6 < 0) f6 = (-f6) * 0.3f;
				
				float f7 = (f6 * 3) - 2;
				if(f7 < 0) {
					float f8 = f7 / 2;
					if(f8 < -1) {
						f8 = -1;
					}
					f = (f8 / 1.4f) / 2.0f;
					f5 = 0;
				}else {
					if (f7 > 1) {
						f7 = 1;
					}
					f = f7 / 8.0f;
				}
				if(f5 < 0) f5 = 0;
				
				float f9 = f5 + 0.5f;
				float f10 = (scaleY / 2) + (((f * scaleY) / 16) * 4);
				
				for(int blockY = 0; blockY < scaleY; ++blockY) {
					float f11 = ((blockY - f10) * 12) / f9;
					if (f11 < 0) {
						f11 *= 4;
					}
					float f12 = this.upperInterpolationNoises[index] / 512f;
					float f13 = this.lowerInterpolationNoises[index] / 512f;
					float f14 = ((this.interpolationNoises[index] / 10) + 1) / 2;

					if (f14 < 0) {
						f2 = f12;
					} else {
						f2 = f14 > 1 ? f13 : f12 + ((f13 - f12) * f14);
					}
					float f15 = f2 - f11;
					if (blockY > scaleY - 4) {
						float f16 = (blockY - (scaleY - 4)) / 3;
						f15 = (f15 * (1 - f16)) + ((-10) * f16);
					}
					heights[index++] = f15;
				}
				
			}
		}
		
		return heights;
	}

	@Override
	public Chunk getChunk(int chunkX, int chunkZ) {
		this.rand.setSeed((int) ((chunkX * 341873128712L) + (chunkZ * 132897987541L))); //TODO maybe 341872712 * chunkX + 132899541 * chunkZ ?
		byte[][][] bArr = new byte[16][16][128];
		this.biomes = this.world.biomeSource.getBiomeBlock(chunkX * 16, chunkZ * 16, 16, 16);
		this.prepareHeights(chunkX, chunkZ, bArr, this.biomes, this.world.biomeSource.temperatureNoises);
		this.buildSurfaces(chunkX, chunkZ, bArr, this.biomes);
		Chunk c = new Chunk(bArr, chunkX, chunkZ);
		return c;
	}

	public void buildSurfaces(int chunkX, int chunkZ, byte[][][] blockIDS, Biome[] biomes) {
		this.sandNoises = this.beachNoise.getRegion(null, chunkX * 16, chunkZ * 16, 0f, 16, 16, 1, 0.03125f, 0.03125f, 1);
        this.gravelNoises = this.beachNoise.getRegion(null, chunkX * 16, 109.01f, chunkZ * 16, 16, 1, 16, 0.03125f, 1, 0.03125f);
        this.surfaceDepthNoises = this.surfaceDepthNoise.getRegion(null, chunkX * 16, chunkZ * 16, 0, 16, 16, 1, 0.0625f, 0.0625f, 0.0625f);
        for (int blockX = 0; blockX < 16; blockX++) {
            for (int blockZ = 0; blockZ < 16; blockZ++) {
                Biome biome = biomes[blockX + (blockZ * 16)];
                boolean z = this.sandNoises[blockX + (blockZ * 16)] + (this.rand.nextFloat() * 0.2f) > 0.0f;
                boolean z2 = this.gravelNoises[blockX + (blockZ * 16)] + (this.rand.nextFloat() * 0.2f) > 3.0f;
                int nextFloat = (int) ((this.surfaceDepthNoises[blockX + (blockZ * 16)] / 3.0f) + 3.0f + (this.rand.nextFloat() * 0.25f));
                int i = -1;
                byte b = biome.topBlock;
                byte b2 = biome.fillerBlock;
                for (int blockY = 127; blockY >= 0; blockY--) {
                	int i2 = (((blockZ * 16) + blockX) * 128) + blockY;
                	if (blockY <= 0 + this.rand.nextInt(5)) {
                		blockIDS[blockX][blockZ][blockY] = (byte) Block.bedrock.blockID;
                    }else {
                    	byte b3 = blockIDS[blockX][blockZ][blockY];
                    	if (b3 == 0) {
                            i = -1;
                        } else if (b3 == Block.stone.blockID) {
                        	if (i == -1) {
                        		if (nextFloat > 0) {
                        			if (blockY >= 64 - 4 && blockY <= 64 + 1) {
                                        b = biome.topBlock;
                                        b2 = biome.fillerBlock;
                                        if (z2) {
                                            b = 0;
                                            b2 = (byte) Block.gravel.blockID;
                                        }
                                        if (z) {
                                            b = (byte) Block.sand.blockID;
                                            b2 = (byte) Block.sand.blockID;
                                        }
                                    }

                        		} else {
                                    b = 0;
                                    b2 = (byte) Block.stone.blockID;
                                }
                        		
                        		if (blockY < 64 && b == 0) {
                        			Logger.info("waterStill");
                                    b = (byte) Block.waterStill.blockID;
                                }
                        		i = nextFloat;
                        		if (blockY >= 64 - 1) {
                        			blockIDS[blockX][blockZ][blockY] = b;
                                } else {
                                	blockIDS[blockX][blockZ][blockY] = b2;
                                }
                        	} else if (i > 0) {
                                i--;
                                blockIDS[blockX][blockZ][blockY] = b2;
                                if (i == 0 && b2 == Block.sand.blockID) {
                                    i = this.rand.nextInt(4);
                                    b2 = (byte) Block.sandStone.blockID;
                                }
                            }

                        }
                    }
                }
            }
        }
		
	}

	@Override
	public void prepareHeights(int chunkX, int chunkZ, byte[][][] blockIDS, Biome[] biomes, float[] temperatures) {
		this.heights = getHeights(null, chunkX * 4, 0, chunkZ * 4, 5, 17, 5);
		
		for (int unkX = 0; unkX < 4; unkX++) {
			for (int unkZ = 0; unkZ < 4; unkZ++) {
				for (int unkY = 0; unkY < 16; unkY++) {
					float f = this.heights[((((unkX + 0) * 5) + unkZ + 0) * 17) + unkY + 0];
					float f2 = this.heights[((((unkX + 0) * 5) + unkZ + 1) * 17) + unkY + 0];
					float f3 = this.heights[((((unkX + 1) * 5) + unkZ + 0) * 17) + unkY + 0];
					float f4 = this.heights[((((unkX + 1) * 5) + unkZ + 1) * 17) + unkY + 0];
					float f5 = (this.heights[((((unkX + 0) * 5) + (unkZ + 0)) * 17) + (unkY + 1)] - f) * 0.125f;
					float f6 = (this.heights[((((unkX + 0) * 5) + (unkZ + 1)) * 17) + (unkY + 1)] - f2) * 0.125f;
					float f7 = (this.heights[((((unkX + 1) * 5) + (unkZ + 0)) * 17) + (unkY + 1)] - f3) * 0.125f;
					float f8 = (this.heights[((((unkX + 1) * 5) + (unkZ + 1)) * 17) + (unkY + 1)] - f4) * 0.125f;
					
					for (int unkYY = 0; unkYY < 8; unkYY++) {
						float f9 = f;
						float f10 = f2;
						float f11 = (f3 - f) * 0.25f;
						float f12 = (f4 - f2) * 0.25f;
						for (int unkXX = 0; unkXX < 4; unkXX++) {
							int i2 = ((unkXX + (unkX * 4)) << 11) | ((0 + (unkZ * 4)) << 7) | ((unkY * 8) + unkYY);
							float f13 = f9;
							float f14 = (f10 - f9) * 0.25f;
							for (int unkZZ = 0; unkZZ < 4; unkZZ++) {
								float d15 = temperatures[(((unkX * 4) + unkXX) * 16) + (unkZ * 4) + unkZZ];
								int i3 = 0;
								if ((unkY * 8) + unkYY < 64) {
									if (d15 < 0.5f && (unkY * 8) + unkYY >= 64 - 1) {
										i3 = Block.ice.blockID;
									} else {
										i3 = Block.waterStill.blockID;
									}
								}
								if (f13 > 0.0f) {
									i3 = Block.stone.blockID;
								}
								blockIDS[(unkXX + (unkX * 4))][(unkZZ + (unkZ * 4))][((unkY * 8) + unkYY)] = (byte) i3;
								i2 += 128;
								f13 += f14;
							}
							f9 += f11;
							f10 += f12;
						}
						f += f5;
						f2 += f6;
						f3 += f7;
						f4 += f8;
					}
				}
			}
		}
	}
}
