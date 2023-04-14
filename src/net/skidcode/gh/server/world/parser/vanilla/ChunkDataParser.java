package net.skidcode.gh.server.world.parser.vanilla;

import java.io.IOException;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.chunk.Chunk;
import net.skidcode.gh.server.world.nbt.NBTFile;

public class ChunkDataParser extends NBTFile{
	public Chunk[] chunks = new Chunk[256];
	public ChunkDataParser(String path2World) throws IOException {
		super(path2World+"/chunks.dat", 0);
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
				assert ch == 71368960; //71368960 - chunk header(04 41 01 20)
				Chunk c = new Chunk();
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						for(int y = 0; y < 128; ++y) {
							c.blockData[x][z][y] = this.getByte();
							if(y % 2 == 0) {
								int prev = this.offset;
								this.offset+=4095;
								byte dd = this.getByte();
								c.blockMetadata[x][z][y] = (byte) (dd & 0xf);
								c.blockMetadata[x][z][y+1] = (byte) (dd >> 0xf);
								this.offset+=2047;
								dd = this.getByte();
								c.blockSkyLight[x][z][y] = (byte) (dd & 0xf);
								c.blockSkyLight[x][z][y+1] = (byte) (dd >> 0xf);
								this.offset+=2047;
								dd = this.getByte();
								c.blockLight[x][z][y] = (byte) (dd & 0xf);
								c.blockLight[x][z][y+1] = (byte) (dd >> 0xf);
								this.offset = prev;
							}
						}
					}
				}
				world.chunks[chunkX][chunkZ] = c;
			}
		}
	}
	
}
