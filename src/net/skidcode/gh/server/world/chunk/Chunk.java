package net.skidcode.gh.server.world.chunk;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.world.LightLayer;

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
	/**
	 * field_228
	 */
	public byte topBlockY;
	
	
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
	
	public void generateHeightMap() { //TODO vanilla
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				byte l = 127;
				//for(int ind = x << 11 | z << 7; l > 0 && Block.lightBlock[this.blockData[(ind + l) - 1] & 0xff] == 0; --l);
				for(;l > 0 && (this.blockData[x << 11 | z << 7 | l-1] & 0xff) == 0;--l);
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
		byte height = this.heightMap[x][z];
		
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
		
		//i suppose it also checks does dimension have sky or not
		if(Block.lightBlock[id] != 0) {
			if(height <= y) this.recalcHeight(x, y + 1, z);
		}else if((height - 1) == y) {
			this.recalcHeight(x, y, z);
		}
		Server.world.updateLight(LightLayer.SKY, worldX, y, worldZ, worldX, y, worldZ);
		
		Server.world.updateLight(LightLayer.BLOCK, worldX, y, worldZ, worldX, y, worldZ);
		this.lightGaps(x, z);
		
		this.setBlockMetadataRaw(x, y, z, meta);
		if(id > 0) {
			Block.blocks[id].onBlockAdded(Server.world, worldX, y, worldZ); //TODO Chunk::world
		}
		
		this.updateMap[x][z] |= 1 << (y >> 4);
		return true;
	}
	
	public void lightGaps(int x, int z) {
		int height = this.heightMap[x][z];
		int worldX = this.posX*16 + x;
		int worldZ = this.posZ*16 + z;
		
		this.lightGap(worldX - 1, worldZ, height);
		this.lightGap(worldX + 1, worldZ, height);
		this.lightGap(worldX, worldZ - 1, height);
		this.lightGap(worldX, worldZ + 1, height);
	}

	public void lightGap(int x, int z, int height) {
		int heightHere = Server.world.getHeightValue(x, z);
		
		if(heightHere < height) {
			Server.world.updateLight(LightLayer.SKY, x, heightHere, z, x, height, z);
		}else if(heightHere != height){
			Server.world.updateLight(LightLayer.SKY, x, height, z, x, heightHere, z);
		}
	}

	public void recalcHeight(int x, int y, int z) {
		byte height = this.heightMap[x][z];
		byte oldHeight = height;
		if(y > height) height = (byte) y;
		
		while(height > 0 && Block.lightBlock[this.getBlockID(x, height - 1, z)] == 0) {
			--height;
		}
		
		if(height != oldHeight) { //TODO multiworld
			Server.world.lightColumnChanged(x, z, height, oldHeight);
			this.heightMap[x][z] = height;
			
			if(this.topBlockY <= height) {
				byte tby = 127;
				for(int i = 0; i < 16; ++i) {
					for(int j = 0; j < 16; ++j) {
						if(this.heightMap[i][j] < tby) tby = this.heightMap[i][j];
					}
				}
				this.topBlockY = tby;
			}else {
				this.topBlockY = height;
			}
			
			int worldX = x + 16*this.posX;
			int worldZ = z + 16*this.posZ;
			
			if(height >= oldHeight) {
				Server.world.updateLight(LightLayer.SKY, worldX, oldHeight, worldZ, worldX, height, worldZ); //TODO dont pass worldX/Z twice?
			
				for(int k = oldHeight; k < height; ++k) {
					this.setSkylightRaw(x, k, z, 0);
				}
			}else {
				for(int k = height; k < oldHeight; ++k) {
					this.setSkylightRaw(x, k, z, 15);
				}
			}
			
			int lightLevel = 15;
			byte savedHeight = height;
			
			while(height > 0 && lightLevel > 0) {
				int lightBlock = Block.lightBlock[this.getBlockID(x, --height, z)];
				
				if(lightBlock == 0) lightBlock = 1;
				
				lightLevel -= lightBlock;
				if(lightLevel < 0) lightLevel = 0;
				
				this.setSkylightRaw(x, height, z, lightLevel);
			}
			
			while(height > 0 && Block.lightBlock[this.getBlockID(x, height - 1, z)] == 0) {
				--height;
			}
			
			if(height != savedHeight) {
				Server.world.updateLight(LightLayer.SKY, worldX - 1, height, worldZ - 1, worldX + 1, savedHeight, worldZ + 1);
			}
		}
		
	}
	
	public boolean setBlockID(int x, int y, int z, int id) {
		int idBefore = this.getBlockID(x, y, z);
		if(idBefore == id) {
			return false;
		}
		int worldX = this.posX*16 + x;
		int worldZ = this.posZ*16 + z;
		byte height = this.heightMap[x][z];
		
		this.blockData[x << 11 | z << 7 | y] = (byte) id;
		
		if(idBefore > 0) {
			Block.blocks[idBefore].onRemove(Server.world, worldX, y, worldZ); //TODO Chunk::world
		}
		this.setBlockMetadataRaw(x, y, z, (byte) 0);

		if(Block.lightBlock[id] != 0) {
			if(y >= height) this.recalcHeight(x, y + 1, z);
		}else if(height - 1 == y) {
			this.recalcHeight(x, y, z);
		}
		//no dim checks here
		Server.world.updateLight(LightLayer.SKY, worldX, y, worldZ, worldX, y, worldZ);
		Server.world.updateLight(LightLayer.BLOCK, worldX, y, worldZ, worldX, y, worldZ);
		this.lightGaps(x, z);
		
		
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
		int index = x << 11 | z << 7 | y;
		return ((index & 1) == 1 ? (this.blockSkyLight[index >> 1] >> 4) : this.blockSkyLight[index >> 1]) & 0xf;
	}
	
	public int getBlocklight(int x, int y, int z) {
		int index = x << 11 | z << 7 | y;
		return ((index & 1) == 1 ? (this.blockLight[index >> 1] >> 4) : this.blockLight[index >> 1]) & 0xf;
	}
	
	public void setSkylightRaw(int x, int y, int z, int level) {
		int index = x << 11 | z << 7 | y;
		if((index & 1) == 1) {
			this.blockSkyLight[index >> 1] &= 0x0f;
			this.blockSkyLight[index >> 1] |= ((level & 0xf) << 4);
		}else {
			this.blockSkyLight[index >> 1] &= 0xf0;
			this.blockSkyLight[index >> 1] |= (level & 0xf);
		}
	}
	
	public void setBlocklightRaw(int x, int y, int z, int level) {
		int index = x << 11 | z << 7 | y;
		if((index & 1) == 1) {
			this.blockLight[index >> 1] &= 0x0f;
			this.blockLight[index >> 1] |= ((level & 0xf) << 4);
		}else {
			this.blockLight[index >> 1] &= 0xf0;
			this.blockLight[index >> 1] |= (level & 0xf);
		}
	}
	
	public int getBlockLight(int x, int y, int z) {
		if(y > 127 || y < 0) return 0;
		int index = x << 11 | z << 7 | y;
		return (index & 1) == 1 ? (this.blockLight[index >> 1] >> 4) : (this.blockLight[index >> 1] & 0xf);
	}

	public int getBrightness(LightLayer layer, int x, int y, int z) {
		switch(layer) {
			case SKY:
				return this.getSkylight(x, y, z);
			case BLOCK:
				return this.getBlocklight(x, y, z);
			default:
				return 0;
		}
	}

	public boolean isSkyLit(int x, int y, int z) {
		return this.heightMap[x][z] <= y;
	}

	public void setBrightness(LightLayer layer, int x, int y, int z, int brightness) {
		switch(layer) {
			case SKY:
				this.setSkylightRaw(x, y, z, brightness);
				break;
			case BLOCK:
				this.setBlocklightRaw(x, y, z, brightness);
				break;
		}
	}
}
