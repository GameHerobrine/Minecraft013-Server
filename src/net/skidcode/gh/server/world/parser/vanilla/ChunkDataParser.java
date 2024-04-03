package net.skidcode.gh.server.world.parser.vanilla;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.world.World;
import net.skidcode.gh.server.world.chunk.Chunk;
import net.skidcode.gh.server.world.data.WorldDataFile;

public class ChunkDataParser extends WorldDataFile{
	public Chunk[] chunks = new Chunk[256];
	public static int[][] locTable = new int[][] { //TODO understand how it works and what it means
		{0x15010000, 0x15160000, 0x152b0000, 0x15400000, 0x15550000, 0x156a0000, 0x157f0000, 0x15940000, 0x15a90000, 0x15be0000, 0x15d30000, 0x15e80000, 0x15fd0000, 0x15120100, 0x15270100, 0x153c0100, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15510100, 0x15660100, 0x157b0100, 0x15900100, 0x15a50100, 0x15ba0100, 0x15cf0100, 0x15e40100, 0x15f90100, 0x150e0200, 0x15230200, 0x15380200, 0x154d0200, 0x15620200, 0x15770200, 0x158c0200, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15a10200, 0x15b60200, 0x15cb0200, 0x15e00200, 0x15f50200, 0x150a0300, 0x151f0300, 0x15340300, 0x15490300, 0x155e0300, 0x15730300, 0x15880300, 0x159d0300, 0x15b20300, 0x15c70300, 0x15dc0300, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15f10300, 0x15060400, 0x151b0400, 0x15300400, 0x15450400, 0x155a0400, 0x156f0400, 0x15840400, 0x15990400, 0x15ae0400, 0x15c30400, 0x15d80400, 0x15ed0400, 0x15020500, 0x15170500, 0x152c0500, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15410500, 0x15560500, 0x156b0500, 0x15800500, 0x15950500, 0x15aa0500, 0x15bf0500, 0x15d40500, 0x15e90500, 0x15fe0500, 0x15130600, 0x15280600, 0x153d0600, 0x15520600, 0x15670600, 0x157c0600, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15910600, 0x15a60600, 0x15bb0600, 0x15d00600, 0x15e50600, 0x15fa0600, 0x150f0700, 0x15240700, 0x15390700, 0x154e0700, 0x15630700, 0x15780700, 0x158d0700, 0x15a20700, 0x15b70700, 0x15cc0700, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15e10700, 0x15f60700, 0x150b0800, 0x15200800, 0x15350800, 0x154a0800, 0x155f0800, 0x15740800, 0x15890800, 0x159e0800, 0x15b30800, 0x15c80800, 0x15dd0800, 0x15f20800, 0x15070900, 0x151c0900, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15310900, 0x15460900, 0x155b0900, 0x15700900, 0x15850900, 0x159a0900, 0x15af0900, 0x15c40900, 0x15d90900, 0x15ee0900, 0x15030a00, 0x15180a00, 0x152d0a00, 0x15420a00, 0x15570a00, 0x156c0a00, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15810a00, 0x15960a00, 0x15ab0a00, 0x15c00a00, 0x15d50a00, 0x15ea0a00, 0x15ff0a00, 0x15140b00, 0x15290b00, 0x153e0b00, 0x15530b00, 0x15680b00, 0x157d0b00, 0x15920b00, 0x15a70b00, 0x15bc0b00, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15d10b00, 0x15e60b00, 0x15fb0b00, 0x15100c00, 0x15250c00, 0x153a0c00, 0x154f0c00, 0x15640c00, 0x15790c00, 0x158e0c00, 0x15a30c00, 0x15b80c00, 0x15cd0c00, 0x15e20c00, 0x15f70c00, 0x150c0d00, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15210d00, 0x15360d00, 0x154b0d00, 0x15600d00, 0x15750d00, 0x158a0d00, 0x159f0d00, 0x15b40d00, 0x15c90d00, 0x15de0d00, 0x15f30d00, 0x15080e00, 0x151d0e00, 0x15320e00, 0x15470e00, 0x155c0e00, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15710e00, 0x15860e00, 0x159b0e00, 0x15b00e00, 0x15c50e00, 0x15da0e00, 0x15ef0e00, 0x15040f00, 0x15190f00, 0x152e0f00, 0x15430f00, 0x15580f00, 0x156d0f00, 0x15820f00, 0x15970f00, 0x15ac0f00, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15c10f00, 0x15d60f00, 0x15eb0f00, 0x15001000, 0x15151000, 0x152a1000, 0x153f1000, 0x15541000, 0x15691000, 0x157e1000, 0x15931000, 0x15a81000, 0x15bd1000, 0x15d21000, 0x15e71000, 0x15fc1000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15111100, 0x15261100, 0x153b1100, 0x15501100, 0x15651100, 0x157a1100, 0x158f1100, 0x15a41100, 0x15b91100, 0x15ce1100, 0x15e31100, 0x15f81100, 0x150d1200, 0x15221200, 0x15371200, 0x154c1200, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15611200, 0x15761200, 0x158b1200, 0x15a01200, 0x15b51200, 0x15ca1200, 0x15df1200, 0x15f41200, 0x15091300, 0x151e1300, 0x15331300, 0x15481300, 0x155d1300, 0x15721300, 0x15871300, 0x159c1300, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x15b11300, 0x15c61300, 0x15db1300, 0x15f01300, 0x15051400, 0x151a1400, 0x152f1400, 0x15441400, 0x15591400, 0x156e1400, 0x15831400, 0x15981400, 0x15ad1400, 0x15c21400, 0x15d71400, 0x15ec1400, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000},
		{0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000}
	};
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
				//assert ch == CHUNK_HEADER; //71368960 - chunk header(04 41 01 20)
				Chunk c = new Chunk(world, chunkX, chunkZ);
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						int index = x << 11 | z << 7;
						System.arraycopy(this.buffer, this.offset, c.blockData, index, 128);
						this.offset += 128;
					}
				}
				for(int x = 0; x < 16; ++x) { //TODO make more beautiful
					for(int z = 0; z < 16; ++z) {
						int index = x << 11 | z << 7;
						System.arraycopy(this.buffer, this.offset, c.blockMetadata, index >> 1, 64);
						this.offset += 64;
					}
				}
				for(int x = 0; x < 16; ++x) { //TODO make more beautiful
					for(int z = 0; z < 16; ++z) {
						int index = x << 11 | z << 7;
						System.arraycopy(this.buffer, this.offset, c.blockSkyLight, index >> 1, 64);
						this.offset += 64;
					}
				}
				for(int x = 0; x < 16; ++x) { //TODO make more beautiful
					for(int z = 0; z < 16; ++z) {
						int index = x << 11 | z << 7;
						System.arraycopy(this.buffer, this.offset, c.blockLight, index >> 1, 64);
						this.offset += 64;
					}
				}
				
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						c.updateMap[x][z] = this.getByte();
					}
				}
				
				world.chunks[chunkX][chunkZ] = c;
				//c.recalcHeightmap(); TODO heightmap
			}
		}
	}

	@Override
	public void save(World world) {
		byte[] buf = new byte[22024192];
		this.setBuffer(buf);
		this.count = 0;
		
		for(int x = 0; x < 32; ++x) {
			for(int z = 0; z < 32; ++z) {
				this.putInt(world.locationTable[x][z]);
			}
		}
		
		for(int chunkX = 0; chunkX < 16; ++chunkX) {
			for(int chunkZ = 0; chunkZ < 16; ++chunkZ) {
				this.count = 4096+(chunkX*21*4096)+(chunkZ*21*16*4096);
				Chunk c = world.chunks[chunkX][chunkZ];
				this.putInt(CHUNK_HEADER);
				
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						int index = x << 11 | z << 7;
						System.arraycopy(c.blockData, index, this.buffer, this.count, 128);
						this.count += 128;
					}
				}
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						int index = x << 11 | z << 7;
						System.arraycopy(c.blockMetadata, index >> 1, this.buffer, this.count, 64);
						this.count += 64;
					}
				}
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						int index = x << 11 | z << 7;
						System.arraycopy(c.blockSkyLight, index >> 1, this.buffer, this.count, 64);
						this.count += 64;
					}
				}
				for(int x = 0; x < 16; ++x) {
					for(int z = 0; z < 16; ++z) {
						int index = x << 11 | z << 7;
						System.arraycopy(c.blockLight, index >> 1, this.buffer, this.count, 64);
						this.count += 64;
					}
				}
				
				for(int x = 0; x < 16; ++x) { //taken from 0.1.0 decomp project (ReMinecraftPE)
					for(int z = 0; z < 16; ++z) {
						this.putByte(c.updateMap[x][z]);
					}
				}
				
			}
		}
		
		
	}
	
}
