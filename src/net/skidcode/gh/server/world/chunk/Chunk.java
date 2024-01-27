package net.skidcode.gh.server.world.chunk;

import net.skidcode.gh.server.utils.Logger;

public class Chunk {
	
	public static EmptyChunk emptyChunk = new EmptyChunk();
	
	public byte[] blockData; //xzy all
	public byte[] blockMetadata = new byte[16384];
	public byte[] blockLight = new byte[16384];
	public byte[] blockSkyLight = new byte[16384];
	public byte[][] heightMap = new byte[16][16];
	public byte[][] updateMap = new byte[16][16];
	public int posX;
	public int posZ;
	
	public Chunk(int x, int z) {
		this(new byte[32768], x, z);
	}
	
	public int getBlockID(int x, int y, int z) {
		if(y > 127 || y < 0) return 0;
		return this.blockData[x << 11 | z << 7 | y] & 0xff;
	}
	public int getBlockMetadata(int x, int y, int z) {
		if(y > 127 || y < 0) return 0;
		int index = x << 11 | z << 7 | y;
		return (index & 1) == 1 ? this.blockMetadata[index >> 1] >> 4 : this.blockMetadata[index >> 1] & 0xf;
	}
	public void setBlockID(int x, int y, int z, byte id) {
		this.blockData[x << 11 | z << 7 | y] = id;
		if(id != 0 && this.heightMap[x][z] < y) {
			this.heightMap[x][z] = (byte) y;
		}
		this.updateMap[x][z] |= 1 << (y >> 4);
	}
	
	public void setBlockMetadata(int x, int y, int z, byte meta) {
		int index = x << 11 | z << 7 | y;
		if((index & 1) == 1) {
			this.blockMetadata[index >> 1] &= 0x0f;
			this.blockMetadata[index >> 1] |= ((meta & 0xf) << 4);
		}else {
			this.blockMetadata[index >> 1] &= 0xf0;
			this.blockMetadata[index >> 1] |= (meta & 0xf);
		}
	}
	
	public void setBlock(int x, int y, int z, byte id) {
		this.setBlock(x, y, z, id, (byte) 0);
	}
	
	public void setBlock(int x, int y, int z, byte id, byte meta) {
		this.setBlockID(x, y, z, id);
		this.setBlockMetadata(x, y, z, meta);
		if(id != 0 && this.heightMap[x][z] < y) {
			this.heightMap[x][z] = (byte) y;
		}
		this.updateMap[x][z] |= 1 << (y >> 4);
	}
	public Chunk(byte[] blockData, int cx, int cz) {
		this.blockData = blockData;
		this.posX = cx;
		this.posZ = cz;
	}
	
	public void clearUpdateMap() {
		this.updateMap = new byte[16][16];
	}
	
	public void generateHeightMap() {
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				byte l = 127;
				
				for(;l > 0 && (this.blockData[x << 11 | z << 7 | l-1] & 0xff) == 0;--l);
				
				heightMap[x][z] = l;
			}
		}
	}
}
