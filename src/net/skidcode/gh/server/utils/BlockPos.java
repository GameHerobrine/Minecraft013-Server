package net.skidcode.gh.server.utils;

public class BlockPos {
	public final int x, y, z;
	public final int hashCode;
	
	public BlockPos(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.hashCode = Utils.packBlockPos(x, y, z);
	}
	
	@Override
	public int hashCode() {
		return this.hashCode;
	}
	
	@Override
	public boolean equals(Object o) {
		BlockPos bp = (BlockPos) o;
		return bp.x == this.x && bp.y == this.y && bp.z == this.z;
	}
}
