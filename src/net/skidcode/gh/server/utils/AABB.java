package net.skidcode.gh.server.utils;
//TODO full implementation
public class AABB {
	
	public float minX, minY, minZ, maxX, maxY, maxZ;
	
	public AABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
}
