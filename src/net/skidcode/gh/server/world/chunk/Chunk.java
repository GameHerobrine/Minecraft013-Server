package net.skidcode.gh.server.world.chunk;

public class Chunk {
	public byte[][][] blockData = new byte[16][16][128]; //xzy all
	public byte[][][] blockMetadata = new byte[16][16][128];
	public byte[][][] blockLight = new byte[16][16][128];
	public byte[][][] blockSkyLight = new byte[16][16][128];
	public byte[][] heightMap = new byte[16][16];
	public byte[][] updateMap = new byte[16][16];
	public int posX;
	public int posZ;
	
	public Chunk(int x, int z) {
		this.posX = x;
		this.posZ = z;
	}
	
	public void setBlockID(int x, int y, int z, byte id) {
		this.blockData[x][z][y] = id;
		if(id != 0 && this.heightMap[x][z] < y) {
			this.heightMap[x][z] = (byte) y;
		}
		this.updateMap[x][z] |= 1 << (y >> 4);
	}
	
	public void setBlockMetadata(int x, int y, int z, byte meta) {
		this.blockMetadata[x][z][y] = meta;
		this.updateMap[x][z] |= 1 << (y >> 4);
	}
	
	public void setBlock(int x, int y, int z, byte id) {
		this.blockData[x][z][y] = id;
		this.blockMetadata[x][z][y] = 0;
		if(id != 0 && this.heightMap[x][z] < y) {
			this.heightMap[x][z] = (byte) y;
		}
		this.updateMap[x][z] |= 1 << (y >> 4);
	}
	
	public void setBlock(int x, int y, int z, byte id, byte meta) {
		this.blockData[x][z][y] = id;
		this.blockMetadata[x][z][y] = meta;
		if(id != 0 && this.heightMap[x][z] < y) {
			this.heightMap[x][z] = (byte) y;
		}
		this.updateMap[x][z] |= 1 << (y >> 4);
	}
	public Chunk(byte[][][] blockData, int cx, int cz) {
		this(cx, cz);
		this.blockData = blockData;
	}
	
	public void clearUpdateMap() {
		this.updateMap = new byte[16][16];
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
