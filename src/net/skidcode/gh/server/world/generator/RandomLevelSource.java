package net.skidcode.gh.server.world.generator;


import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.noise.PerlinNoise;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.biome.Biome;
import net.skidcode.gh.server.world.chunk.Chunk;
import net.skidcode.gh.server.world.feature.CactusFeature;
import net.skidcode.gh.server.world.feature.ClayFeature;
import net.skidcode.gh.server.world.feature.Feature;
import net.skidcode.gh.server.world.feature.FlowerFeature;
import net.skidcode.gh.server.world.feature.OreFeature;
import net.skidcode.gh.server.world.feature.ReedsFeature;
import net.skidcode.gh.server.world.feature.SpringFeature;

public class RandomLevelSource implements LevelSource{
	private World world;
	private BedrockRandom rand;
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
		this.rand = new BedrockRandom(seed);
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
		this.interpolationNoises = this.interpolationNoise.getRegion(null, chunkX, chunkY, chunkZ, scaleX, scaleY, scaleZ, 684.41f / 80.0f, 684.41f / 160.0f, 684.41f / 80.0f);
		this.upperInterpolationNoises = this.upperInterpolationNoise.getRegion(null, chunkX, chunkY, chunkZ, scaleX, scaleY, scaleZ, 684.41f, 684.41f, 684.41f);
		this.lowerInterpolationNoises = this.lowerInterpolationNoise.getRegion(null, chunkX, chunkY, chunkZ, scaleX, scaleY, scaleZ, 684.41f, 684.41f, 684.41f);
		
		int k1 = 0;
		int l1 = 0;
		int i2 = 16 / scaleX;
		for(int x = 0; x < scaleX; x++)
		{
			int xNoiseIndex = x * i2 + i2 / 2;
			for(int z = 0; z < scaleZ; z++)
			{
				int zNoiseIndex = z * i2 + i2 / 2;
				float tempValue = tempNoises[xNoiseIndex * 16 + zNoiseIndex];
				float rainValue = rainNoises[xNoiseIndex * 16 + zNoiseIndex] * tempValue;
				float d4 = 1.0F - rainValue;
				d4 *= d4;
				d4 *= d4;
				d4 = 1.0F - d4;
				float f5 = (biomeNoises[l1] + 256F) / 512F;
				f5 *= d4;
				if(f5 > 1.0F)
				{
					f5 = 1.0F;
				}
				float f6 = depthNoises[l1] / 8000F;
				if(f6 < 0.0F)
				{
					f6 = f6 * -0.3f;
				}
				f6 = f6 * 3F - 2F;
				if(f6 < 0.0F)
				{
					f6 /= 2f;
					if(f6 < -1f)
					{
						f6 = -1F;
					}
					f6 /= 1.4f;
					f6 /= 2f;
					f5 = 0.0F;
				} else
				{
					if(f6 > 1.0F)
					{
						f6 = 1.0F;
					}
					f6 /= 8f;
				}
				if(f5 < 0.0F)
				{
					f5 = 0.0F;
				}
				f5 += 0.5F;
				f6 = (f6 * (float)scaleY) / 16F;
				float d7 = (float)scaleY / 2F + f6 * 4F;
				l1++;
				for(int y = 0; y < scaleY; y++)
				{
					float d8 = 0.0F;
					float d9 = (((float)y - d7) * 12F) / f5;
					if(d9 < 0.0F)
					{
						d9 *= 4f;
					}
					float d10 = upperInterpolationNoises[k1] / 512F;
					float d11 = lowerInterpolationNoises[k1] / 512F;
					float d12 = (interpolationNoises[k1] / 10F + 1.0F) / 2F;
					if(d12 < 0.0F)
					{
						d8 = d10;
					} else
					if(d12 > 1.0F)
					{
						d8 = d11;
					} else
					{
						d8 = d10 + (d11 - d10) * d12;
					}
					d8 -= d9;
					if(y > scaleY - 4)
					{
						float d13 = (float)(y - (scaleY - 4)) / 3F;
						d8 = d8 * (1.0F - d13) + -10F * d13;
					}
					heights[k1] = (float) d8;
					k1++;
				}

			}

		}
		
		return heights;
	}

	@Override
	public Chunk getChunk(int chunkX, int chunkZ) {
		this.rand.setSeed((int) (341872712 * chunkX + 132899541 * chunkZ));
		byte[] bArr = new byte[32768];
		this.biomes = this.world.biomeSource.getBiomeBlock(chunkX * 16, chunkZ * 16, 16, 16);
		this.prepareHeights(chunkX, chunkZ, bArr, this.biomes, this.world.biomeSource.temperatureNoises);
		this.buildSurfaces(chunkX, chunkZ, bArr, this.biomes);
		Chunk c = new Chunk(bArr, chunkX, chunkZ);
		c.recalcHeightmap();
		return c;
	}

	public void buildSurfaces(int chunkX, int chunkZ, byte[] blockIDS, Biome[] biomes) {
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
				for (int blockY = 127; blockY >= 0; --blockY) {
					int index = (blockZ * 16 + blockX) * 128 + blockY;
					if (this.rand.nextInt(5) >= blockY) {
						blockIDS[index] = (byte) Block.bedrock.blockID;
					}else {
						byte b3 = blockIDS[index];
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
									b = (byte) Block.waterStill.blockID;
								}
								i = nextFloat;
								if (blockY >= 64 - 1) {
									blockIDS[index] = b;
								} else {
									blockIDS[index] = b2;
								}
							} else if (i > 0) {
								i--;
								blockIDS[index] = b2;
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
	public void prepareHeights(int chunkX, int chunkZ, byte[] blockIDS, Biome[] biomes, float[] temperatures) {
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
							int index = (unkXX + (unkX * 4)) << 11 | unkZ*4 << 7 | unkY*8+unkYY;
							float f13 = f9;
							float f14 = (f10 - f9) * 0.25f;
							for (int unkZZ = 0; unkZZ < 4; unkZZ++) {
								float d15 = temperatures[(((unkX * 4) + unkXX) * 16) + (unkZ * 4) + unkZZ];
								int i3 = 0;
								if ((unkY * 8) + unkYY < 64) {
									if (d15 < 0.5f && (unkY * 8) + unkYY >= 63) {
										i3 = Block.ice.blockID;
									} else {
										i3 = Block.waterStill.blockID;
									}
								}
								if (f13 > 0.0f) {
									i3 = Block.stone.blockID;
								}
								blockIDS[index] = (byte) i3;
								index += 128;
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

	@Override
	public void postProcess(int chunkX, int chunkZ) {
		int chunkXWorld = chunkX * 16;
		int chunkZWorld = chunkZ * 16;
		Biome biome = this.world.biomeSource.getBiome(chunkXWorld + 16, chunkZWorld + 16);
		this.rand.setSeed(this.world.worldSeed);
		int i1 = (rand.nextInt() / 2) * 2 + 1;
		int j1 = (rand.nextInt() / 2) * 2 + 1;
		rand.setSeed(chunkX * i1 + chunkZ * j1 ^ world.worldSeed);
		for (int i2 = 0; i2 < 10; i2++) {
			new ClayFeature(32).place(this.world, this.rand, chunkXWorld + this.rand.nextInt(16), this.rand.nextInt(128), chunkZWorld + this.rand.nextInt(16));
		}
		for (int i3 = 0; i3 < 20; i3++) {
			new OreFeature(Block.dirt.blockID, 32).place(this.world, this.rand, chunkXWorld + this.rand.nextInt(16), this.rand.nextInt(128), chunkZWorld + this.rand.nextInt(16));
		}
		for (int i4 = 0; i4 < 10; i4++) {
			new OreFeature(Block.gravel.blockID, 32).place(this.world, this.rand, chunkXWorld + this.rand.nextInt(16), this.rand.nextInt(128), chunkZWorld + this.rand.nextInt(16));
		}
		for (int i5 = 0; i5 < 20; i5++) {
			new OreFeature(Block.oreCoal.blockID, 16).place(this.world, this.rand, chunkXWorld + this.rand.nextInt(16), this.rand.nextInt(128), chunkZWorld + this.rand.nextInt(16));
		}
		for (int i6 = 0; i6 < 20; i6++) {
			new OreFeature(Block.oreIron.blockID, 8).place(this.world, this.rand, chunkXWorld + this.rand.nextInt(16), this.rand.nextInt(64), chunkZWorld + this.rand.nextInt(16));
		}
		for (int i7 = 0; i7 < 2; i7++) {
			new OreFeature(Block.oreGold.blockID, 8).place(this.world, this.rand, chunkXWorld + this.rand.nextInt(16), this.rand.nextInt(32), chunkZWorld + this.rand.nextInt(16));
		}
		for (int i8 = 0; i8 < 8; i8++) {
			new OreFeature(Block.redstoneOre.blockID, 7).place(this.world, this.rand, chunkXWorld + this.rand.nextInt(16), this.rand.nextInt(16), chunkZWorld + this.rand.nextInt(16));
		}
		for (int i9 = 0; i9 < 1; i9++) {
			new OreFeature(Block.diamondOre.blockID, 7).place(this.world, this.rand, chunkXWorld + this.rand.nextInt(16), this.rand.nextInt(16), chunkZWorld + this.rand.nextInt(16));
		}
		for (int i10 = 0; i10 < 1; i10++) {
			new OreFeature(Block.lapisOre.blockID, 6).place(this.world, this.rand, chunkXWorld + this.rand.nextInt(16), this.rand.nextInt(16) + this.rand.nextInt(16), chunkZWorld + this.rand.nextInt(16));
		}

		int sample = (int) ((((this.treeNoise.getValue(chunkXWorld * 0.5f, chunkZWorld * 0.5f) / 8.0f) + (this.rand.nextFloat() * 4.0f)) + 4.0f) / 3.0f);
		int treesAmount = this.rand.nextInt(10) == 0 ? 1 : 0;
		if(biome == Biome.forest)
		{
			treesAmount += sample + 2;
		}
		if(biome == Biome.rainForest)
		{
			treesAmount += sample + 2;
		}
		if(biome == Biome.seasonalForest)
		{
			treesAmount += sample + 1;
		}
		if(biome == Biome.taiga)
		{
			treesAmount += sample + 1;
		}
		if(biome == Biome.desert)
		{
			treesAmount -= 20;
		}
		if(biome == Biome.tundra)
		{
			treesAmount -= 20;
		}
		if(biome == Biome.plains)
		{
			treesAmount -= 20;
		}
		for(int l8 = 0; l8 < treesAmount; l8++)
		{
			int l12 = chunkXWorld + rand.nextInt(16) + 8;
			int j15 = chunkZWorld + rand.nextInt(16) + 8;
			Feature tree = biome.getTreeFeature(rand);
			//tree.func_517_a(1.0D, 1.0D, 1.0D);
			int hm = this.world.getHeightmap(l12, j15);
			Logger.info(String.format("Trying to generate a tree on %d %d %d ", l12, hm, j15));
			tree.place(this.world, rand, l12, hm, j15);
		}
		for(int i9 = 0; i9 < 2; i9++)
		{
			int i13 = chunkXWorld + rand.nextInt(16) + 8;
			int k15 = rand.nextInt(128);
			int l17 = chunkZWorld + rand.nextInt(16) + 8;
			(new FlowerFeature(Block.flower.blockID)).place(this.world, rand, i13, k15, l17);
		}

		if(rand.nextInt(2) == 0)
		{
			int j9 = chunkXWorld + rand.nextInt(16) + 8;
			int j13 = rand.nextInt(128);
			int l15 = chunkZWorld + rand.nextInt(16) + 8;
			(new FlowerFeature(Block.rose.blockID)).place(this.world, rand, j9, j13, l15);
		}
		if(rand.nextInt(4) == 0)
		{
			int k9 = chunkXWorld + rand.nextInt(16) + 8;
			int k13 = rand.nextInt(128);
			int i16 = chunkZWorld + rand.nextInt(16) + 8;
			(new FlowerFeature(Block.mushroomRed.blockID)).place(this.world, rand, k9, k13, i16);
		}
		if(rand.nextInt(8) == 0)
		{
			int l9 = chunkXWorld + rand.nextInt(16) + 8;
			int l13 = rand.nextInt(128);
			int j16 = chunkZWorld + rand.nextInt(16) + 8;
			(new FlowerFeature(Block.mushroomBrown.blockID)).place(this.world, rand, l9, l13, j16);
		}
		
		for(int i10 = 0; i10 < 10; i10++)
		{
			int i14 = chunkXWorld + rand.nextInt(16) + 8;
			int k16 = rand.nextInt(128);
			int i18 = chunkZWorld + rand.nextInt(16) + 8;
			(new ReedsFeature()).place(this.world, rand, i14, k16, i18);
		}
		
		int cactiAmount = 0;
		if(biome == Biome.desert)
		{
			cactiAmount += 5;
		}
		for(int j14 = 0; j14 < cactiAmount; j14++)
		{
			int l16 = chunkXWorld + rand.nextInt(16) + 8;
			int j18 = rand.nextInt(128);
			int j19 = chunkZWorld + rand.nextInt(16) + 8;
			(new CactusFeature()).place(this.world, rand, l16, j18, j19);
		}
		
		for(int k14 = 0; k14 < 50; k14++)
		{
			int i17 = chunkXWorld + rand.nextInt(16) + 8;
			int k18 = rand.nextInt(rand.nextInt(120) + 8);
			int k19 = chunkZWorld + rand.nextInt(16) + 8;
			(new SpringFeature(Block.waterFlowing.blockID)).place(this.world, rand, i17, k18, k19);
		}

		for(int l14 = 0; l14 < 20; l14++)
		{
			int j17 = chunkXWorld + rand.nextInt(16) + 8;
			int l18 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
			int l19 = chunkZWorld + rand.nextInt(16) + 8;
			(new SpringFeature(Block.lavaFlowing.blockID)).place(this.world, rand, j17, l18, l19);
		}
		
		float[] temps = this.world.biomeSource.getTemperatureBlock(chunkXWorld + 8, chunkZWorld + 8, 16, 16);
		for(int i15 = chunkXWorld + 8; i15 < chunkXWorld + 8 + 16; i15++)
		{
			for(int k17 = chunkZWorld + 8; k17 < chunkZWorld + 8 + 16; k17++)
			{
				int i19 = i15 - (chunkXWorld + 8);
				int i20 = k17 - (chunkZWorld + 8);
				int j20 = this.world.findTopSolidBlock(i15, k17);
				float d1 = temps[i19 * 16 + i20] - ((j20 - 64) / 64f) * 0.3f;
				if(d1 < 0.5D && j20 > 0 && j20 < 128 && this.world.isAirBlock(i15, j20, k17) && this.world.getMaterial(i15, j20 - 1, k17).isSolid && this.world.getMaterial(i15, j20 - 1, k17) != Material.ice)
				{
					this.world.placeBlock(i15, j20, k17, (byte) Block.snowLayer.blockID);
				}
			}
		}
	}
}
