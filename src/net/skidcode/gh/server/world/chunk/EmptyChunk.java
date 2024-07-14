package net.skidcode.gh.server.world.chunk;

import java.util.Arrays;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.world.LightLayer;

public class EmptyChunk extends Chunk{
	public EmptyChunk() {
		super(null, 16, 16);
		Arrays.fill(this.blockData, (byte)Block.invisibleBedrock.blockID);
	}
	@Override
	public void setSkylightRaw(int x, int y, int z, int level) {}
	@Override
	public void setBlocklightRaw(int x, int y, int z, int level) {}
	@Override
	public void setBrightness(LightLayer layer, int x, int y, int z, int brightness) {}
	@Override
	public boolean isSkyLit(int x, int y, int z) {
		return false;
	}
	public void lightGap(int x, int z, int height) {}
	public void recalcHeight(int x, int y, int z) {}
}
