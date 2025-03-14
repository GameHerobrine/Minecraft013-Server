package net.skidcode.gh.server.world.generator;

import java.util.stream.IntStream;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.chunk.Chunk;
import net.skidcode.gh.server.world.parser.vanilla.ChunkDataParser;

public class FlatWorldGenerator {
	public static void generateChunks(World w) {
		for(int x = 0; x < 16; ++x) {
			for(int z = 0; z < 16; ++z) {
				Chunk c = new Chunk(w, x, z);
				FlatWorldGenerator.generateChunk(c);
				w.chunks[x][z] = c;
			}
			Logger.info("Generating "+x+": [0-15] chunks");
		}
		
		w.locationTable = ChunkDataParser.locTable;
	}
	
	public static void generateChunk(Chunk c) {
		for(int xz = 0; xz < 256; ++xz) {
			int x = xz & 0xf;
			int z = xz >> 4;
			/*for(int y = 127; y >= 0; --y) {
				if(y == 3) {
					c.blockData[x][z][y] = (byte) Block.grass.blockID;
					c.blockMetadata[x][z][y] = 0;
				}else if(y == 0) {
					c.blockData[x][z][y] = (byte) Block.bedrock.blockID;
					c.blockMetadata[x][z][y] = 0;
				}else if(y < 3) {
					c.blockData[x][z][y] = (byte) Block.dirt.blockID;
					c.blockMetadata[x][z][y] = 0;
				}
			}*/
			// Why would we need loops for flat world?
			c.setBlock(x, 3, z, (byte)Block.grass.blockID, (byte)0);
			c.setBlock(x, 2, z, (byte)Block.dirt.blockID, (byte)0);
			c.setBlock(x, 1, z, (byte)Block.dirt.blockID, (byte)0);
			c.setBlock(x, 0, z, (byte)Block.bedrock.blockID, (byte)0);
		}
	}
}
