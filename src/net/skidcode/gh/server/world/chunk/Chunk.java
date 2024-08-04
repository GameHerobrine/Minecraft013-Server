package net.skidcode.gh.server.world.chunk;

import java.util.Arrays;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.world.LightLayer;
import net.skidcode.gh.server.world.World;

public class Chunk {
	
	public static EmptyChunk emptyChunk = new EmptyChunk();
	public World world;
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
	
	
	public Chunk(World world, int x, int z) {
		this(world, new byte[32768], x, z);
	}

	public Chunk(World world, byte[] blockData, int cx, int cz) {
		this.world = world;
		this.blockData = blockData;
		this.posX = cx;
		this.posZ = cz;
	}
	
	public void clearUpdateMap() {
		this.updateMap = new byte[16][16];
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
				b.onRemove(this.world, worldX, y, worldZ);
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
		this.world.updateLight(LightLayer.SKY, worldX, y, worldZ, worldX, y, worldZ);
		
		this.world.updateLight(LightLayer.BLOCK, worldX, y, worldZ, worldX, y, worldZ);
		this.lightGaps(x, z);
		
		this.setBlockMetadataRaw(x, y, z, meta);
		if(id > 0) {
			Block.blocks[id].onBlockAdded(this.world, worldX, y, worldZ);
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
		int heightHere = this.world.getHeightmap(x, z);
		
		if(heightHere < height) {
			this.world.updateLight(LightLayer.SKY, x, heightHere, z, x, height, z);
		}else if(heightHere != height){
			this.world.updateLight(LightLayer.SKY, x, height, z, x, heightHere, z);
		}
	}

	public void recalcHeight(int x, int y, int z) {
		int height = this.heightMap[x][z] & 0xff;
		int oldHeight = height & 0xff;
		if(y > height) height = y & 0xff;
		
		while(height > 0 && Block.lightBlock[this.getBlockID(x, height - 1, z)] == 0) {
			--height;
		}
		
		if(height != oldHeight) {
			this.world.lightColumnChanged(x, z, height, oldHeight);
			this.heightMap[x][z] = (byte)height;
			
			if(this.topBlockY <= height) {
				byte tby = 127;
				for(int i = 0; i < 16; ++i) {
					for(int j = 0; j < 16; ++j) {
						if(this.heightMap[i][j] < tby) tby = this.heightMap[i][j];
					}
				}
				this.topBlockY = tby;
			}else {
				this.topBlockY = (byte)height;
			}
			
			int worldX = x + 16*this.posX;
			int worldZ = z + 16*this.posZ;
			
			if(height >= oldHeight) {
				this.world.updateLight(LightLayer.SKY, worldX, oldHeight, worldZ, worldX, height, worldZ);
			
				for(int k = oldHeight; k < height; ++k) {
					this.setSkylightRaw(x, k, z, 0);
				}
			}else {
				for(int k = height; k < oldHeight; ++k) {
					this.setSkylightRaw(x, k, z, 15);
				}
			}
			
			int lightLevel = 15;
			int savedHeight = height;
			
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
				this.world.updateLight(LightLayer.SKY, worldX - 1, height, worldZ - 1, worldX + 1, savedHeight, worldZ + 1);
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
			Block.blocks[idBefore].onRemove(this.world, worldX, y, worldZ);
		}
		this.setBlockMetadataRaw(x, y, z, (byte) 0);

		if(Block.lightBlock[id] != 0) {
			if(y >= height) this.recalcHeight(x, y + 1, z);
		}else if(height - 1 == y) {
			this.recalcHeight(x, y, z);
		}
		//no dim checks here
		this.world.updateLight(LightLayer.SKY, worldX, y, worldZ, worldX, y, worldZ);
		this.world.updateLight(LightLayer.BLOCK, worldX, y, worldZ, worldX, y, worldZ);
		this.lightGaps(x, z);
		
		
		if(id > 0) {
			Block.blocks[id].onBlockAdded(this.world, worldX, y, worldZ);
		}
		
		
		this.updateMap[x][z] |= 1 << (y >> 4);
		return true;
	}
	
	public int getBlockID(int x, int y, int z) {
		return this.blockData[(x & 0xf) << 11 | (z & 0xf) << 7 | (y & 0x7f)] & 0xff;
	}
	public int getBlockMetadata(int x, int y, int z) {
		int index = x << 11 | z << 7 | y;
		return ((index & 1) == 1 ? (this.blockMetadata[index >> 1] >> 4) : this.blockMetadata[index >> 1])  & 0xf;
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

	public void recalcHeightmapOnly() {
		int topBlock = 127;
		for(int x = 0; x <= 15; ++x) {
			for(int z = 0; z <= 15; ++z) {
				int y = 127;
				int xzIndex = (x << 11) | (z << 7);
				
				while(y > 0 && Block.lightBlock[this.blockData[y - 1 + xzIndex]] == 0) {
					--y;
				}
				
				this.heightMap[x][z] = (byte) y;
				if(y < topBlock) topBlock = y;
			}
		}
		this.topBlockY = (byte) topBlock;
	}
	
	public void recalcHeightmap() {
		int topBlock = 127;
		for(int x = 0; x <= 15; ++x) {
			for(int z = 0; z <= 15; ++z) {
				int y = 127;
				int xzIndex = (x << 11) | (z << 7);
				
				while(y > 0 && Block.lightBlock[this.blockData[y - 1 + xzIndex]] == 0) {
					--y;
				}
				
				this.heightMap[x][z] = (byte) y;
				if(y < topBlock) topBlock = y;
				
				//dim -> hasNoSkyCheck start?
				int lightLevel = 15;
				int yLight = 127;
				do {
					lightLevel -= Block.lightBlock[this.blockData[yLight + xzIndex]];
					if(lightLevel > 0) this.setSkylightRaw(x, yLight, z, lightLevel);
				}while(--yLight > 0 && lightLevel > 0);
			}
		}
		
		this.topBlockY = (byte) topBlock;
		for(int x = 0; x <= 15; ++x) {
			for(int z = 0; z <= 15; ++z) {
				this.lightGaps(x, z);
			}
		}
	}

	public int getHeightmap(int x, int z) {
		return this.heightMap[x][z];
	}

	public int getRawBrightness(int x, int y, int z, int k) {
		
		int sky = this.getSkylight(x, y, z);
		//if(sky > 0) Chunk.touchedSky = 1, useless probably?
		
		sky -= k;
		int block = this.getBlocklight(x, y, z);
		
		return block > sky ? block : sky;
	}
}
