package net.skidcode.gh.server.utils;

public class AxisAlignedBB {
	public float minX, minY, minZ;
	public float maxX, maxY, maxZ;
	
	public AxisAlignedBB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public void setBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	
	@Override
	public String toString() {
		return String.format("AxisAlignedBB(minXYZ={%f %f %f}, maxXYZ={%f %f %f})", this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}
}
