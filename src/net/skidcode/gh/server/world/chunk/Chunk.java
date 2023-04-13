package net.skidcode.gh.server.world.chunk;

public class Chunk {
	public byte[][][] blockData = new byte[16][16][128];
	public byte[][][] blockMetadata = new byte[16][16][128];
	public byte[][][] blockLight = new byte[16][16][128];
	public byte[][][] blockSkyLight = new byte[16][16][128];
	
}
