package net.skidcode.gh.server.world.parser.vanilla;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.chunk.Chunk;
import net.skidcode.gh.server.world.nbt.WorldNBTFile;

public class ChunkDataParser extends WorldNBTFile{
	public Chunk[] chunks = new Chunk[256];
	
	public static final int CHUNK_HEADER = 71368960;
	
	public ChunkDataParser(Path p) throws IOException {
		super(p, 0);
	}

	public ChunkDataParser(String string) throws IOException {
		super(string+"/chunks.dat", 0);
	}

	@Override
	public void parse(World world) {
		
		int[][] locationTable = new int[32][32];
		
		for(int x = 0; x < 32; ++x) {
			for(int z = 0; z < 32; ++z) {
				locationTable[x][z] = this.getInt();
			}
		}
		
		world.locationTable = locationTable;
		
		//offset -> 0x1000
		for(int chunkX = 0; chunkX < 16; ++chunkX) {
			for(int chunkZ = 0; chunkZ < 16; ++chunkZ) {
				this.offset = 4096+(chunkX*21*4096)+(chunkZ*21*16*4096);
				
				
				int ch = this.getInt();
				assert ch == CHUNK_HEADER; //71368960 - chunk header(04 41 01 20)
				Chunk c = new Chunk(chunkX, chunkZ);
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						for(int y = 0; y < 128; ++y) {
							c.blockData[x][z][y] = this.getByte();
						}
					}
				}
				for(int x = 0; x < 16; ++x) { //TODO make more beautiful
					for(int z = 0; z < 16; ++z) {
						for(int y = 0; y < 64; ++y) {
							byte dd = this.getByte();
							c.blockMetadata[x][z][y*2] = (byte) (dd & 0xf);
							c.blockMetadata[x][z][y*2+1] = (byte) (dd >> 0x4);
						}
					}
				}
				for(int x = 0; x < 16; ++x) { //TODO make more beautiful
					for(int z = 0; z < 16; ++z) {
						for(int y = 0; y < 64; ++y) {
							byte dd = this.getByte();
							c.blockSkyLight[x][z][y*2] = (byte) (dd & 0xf);
							c.blockSkyLight[x][z][y*2+1] = (byte) (dd >> 0x4);
						}
					}
				}
				for(int x = 0; x < 16; ++x) { //TODO make more beautiful
					for(int z = 0; z < 16; ++z) {
						for(int y = 0; y < 64; ++y) {
							byte dd = this.getByte();
							c.blockLight[x][z][y*2] = (byte) (dd & 0xf);
							c.blockLight[x][z][y*2+1] = (byte) (dd >> 0x4);
						}
					}
				}
				world.chunks[chunkX][chunkZ] = c;
			}
		}
	}

	@Override
	public void save(World world) {
		byte[] buf = new byte[22024192];
		this.setBuffer(buf);
		this.count = 0;
		
		for(int[] zloc : world.locationTable) {
			for(int loc : zloc) {
				this.putInt(loc);
			}
		}
		
		for(int chunkX = 0; chunkX < 16; ++chunkX) { //TODO should save 32x32?
			for(int chunkZ = 0; chunkZ < 16; ++chunkZ) {
				this.count = 4096+(chunkX*21*4096)+(chunkZ*21*16*4096);
				Chunk c = world.chunks[chunkX][chunkZ];
				this.putInt(CHUNK_HEADER);
				
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						for(int y = 0; y < 128; ++y) {
							this.putByte(c.blockData[x][z][y]);
						}
					}
				}
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						for(int y = 0; y < 64; ++y) {
							this.putByte((byte) (c.blockMetadata[x][z][y*2] + (c.blockMetadata[x][z][y*2+1] << 4)));
						}
					}
				}
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						for(int y = 0; y < 64; ++y) {
							this.putByte((byte) (c.blockSkyLight[x][z][y*2] + (c.blockSkyLight[x][z][y*2+1] << 4)));
						}
					}
				}
				for(int x = 0; x < 16; ++x) { //TODO make more beautiful
					for(int z = 0; z < 16; ++z) {
						for(int y = 0; y < 64; ++y) {
							this.putByte((byte) (c.blockLight[x][z][y*2] + (c.blockLight[x][z][y*2+1] << 4)));
						}
					}
				}
			}
		}
		
		
	}
	
}
