package net.skidcode.gh.server.world.chunk;

public class Chunk {
	public byte[][][] blockData = new byte[16][16][128]; //xzy all
	public byte[][][] blockMetadata = new byte[16][16][128];
	public byte[][][] blockLight = new byte[16][16][128];
	public byte[][][] blockSkyLight = new byte[16][16][128];
	
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
}
