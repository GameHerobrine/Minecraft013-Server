package net.skidcode.gh.server.world.chunk;

import java.util.Arrays;

import net.skidcode.gh.server.block.Block;

public class EmptyChunk extends Chunk{
	public EmptyChunk() {
		super(16, 16);
		Arrays.fill(this.blockData, (byte)Block.invisibleBedrock.blockID);
	}
}
