package net.skidcode.gh.server.world.chunk;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.block.Block;
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
				for(int ind = x << 11 | z << 7; l > 0 && Block.lightBlock[this.blockData[(ind + l) - 1] & 0xff] == 0; --l);
				heightMap[x][z] = l;
			}
		}
	}
	
	
	public void setBlockMetadataRaw(int x, int y, int z, byte meta) {
		int index = x << 11 | z << 7 | y;
		if((index & 1) == 1) {
			this.blockMetadata[index >> 1] &= 0x0f;
			this.blockMetadata[index >> 1] |= ((meta & 0xf) << 4);
		}else {
			this.blockMetadata[index >> 1] &= 0xf0;
			this.blockMetadata[index >> 1] |= (meta & 0xf);
		}
	}
	
	public boolean setBlock(int x, int y, int z, byte id, byte meta) {
		
		int idBefore = this.getBlockID(x, y, z) & 0xff;
		if(idBefore == id) {
			if(this.getBlockMetadata(x, y, z) == meta) return false;
		}
		
		int worldX = this.posX*16 + x;
		int worldZ = this.posZ*16 + z;
		
		this.blockData[x << 11 | z << 7 | y] = (byte) id;
		
		if(idBefore > 0) {
			Block b = Block.blocks[idBefore];
			if(b != null) {
				b.onRemove(Server.world, worldX, y, worldZ); //TODO Chunk::world
			}else {
				Logger.warn(String.format("%d-%d-%d has unknown block ID(%d)!", worldX, y, worldZ, idBefore));
			}
			
			//Removal of TileEntities is also handled here, but they didnt exist until ~0.3
		}
		this.setBlockMetadataRaw(x, y, z, meta);
		
		//TODO light
		if(id != 0 && this.heightMap[x][z] < y) {
			this.heightMap[x][z] = (byte) y;
		}
		
		if(id > 0) {
			Block.blocks[id].onBlockAdded(Server.world, worldX, y, worldZ); //TODO Chunk::world
		}
		
		this.updateMap[x][z] |= 1 << (y >> 4);
		return true;
	}
	
	public boolean setBlockID(int x, int y, int z, int id) {
		int idBefore = this.getBlockID(x, y, z);
		if(idBefore == id) {
			return false;
		}
		
		int worldX = this.posX*16 + x;
		int worldZ = this.posZ*16 + z;
		
		this.blockData[x << 11 | z << 7 | y] = (byte) id;
		
		if(idBefore > 0) {
			Block.blocks[idBefore].onRemove(Server.world, worldX, y, worldZ); //TODO Chunk::world
		}
		
		this.setBlockMetadataRaw(x, y, z, (byte) 0);
		//TODO light
		if(id != 0 && this.heightMap[x][z] < y) { //TODO move to special funcion
			this.heightMap[x][z] = (byte) y;
		}
		
		if(id > 0) {
			Block.blocks[id].onBlockAdded(Server.world, worldX, y, worldZ); //TODO Chunk::world
		}
		
		
		this.updateMap[x][z] |= 1 << (y >> 4);
		return true;
	}
	
	public int getBlockID(int x, int y, int z) {
		return this.blockData[(x & 0xf) << 11 | (z & 0xf) << 7 | (y & 0x7f)] & 0xff;
	}
	public int getBlockMetadata(int x, int y, int z) {
		if(y > 127 || y < 0) return 0;
		int index = x << 11 | z << 7 | y;
		return (index & 1) == 1 ? (this.blockMetadata[index >> 1] >> 4) : (this.blockMetadata[index >> 1] & 0xf);
	}
	
	public int getSkylight(int x, int y, int z) {
		if(y > 127 || y < 0) return 0;
		int index = x << 11 | z << 7 | y;
		return (index & 1) == 1 ? (this.blockSkyLight[index >> 1] >> 4) : (this.blockSkyLight[index >> 1] & 0xf);
	}
	
	public int getBlockLight(int x, int y, int z) {
		if(y > 127 || y < 0) return 0;
		int index = x << 11 | z << 7 | y;
		return (index & 1) == 1 ? (this.blockLight[index >> 1] >> 4) : (this.blockLight[index >> 1] & 0xf);
	}
	
	
}
