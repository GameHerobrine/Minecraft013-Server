package net.skidcode.gh.server.world.chunk;

import net.skidcode.gh.server.utils.Logger;

public class Chunk {
	public byte[][][] blockData = new byte[16][16][128]; //xzy all
	public byte[][][] blockMetadata = new byte[16][16][128];
	public byte[][][] blockLight = new byte[16][16][128];
	public byte[][][] blockSkyLight = new byte[16][16][128];
	public byte[][] heightMap = new byte[16][16];
	public int posX;
	public int posZ;
	
	public Chunk(int x, int z) {
		this.posX = x;
		this.posZ = z;
	}
	
	public Chunk(byte[][][] blockData, int cx, int cz) {
		this(cx, cz);
		this.blockData = blockData;
	}
	
	public void generateHeightMap() {
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				byte l = 127;
				for(;l > 0 && (blockData[x][z][l-1] & 0xff) == 0;l--);
				
				heightMap[x][z] = l;
			}
		}
	}
}
