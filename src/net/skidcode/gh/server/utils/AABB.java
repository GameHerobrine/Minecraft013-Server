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
	
	public boolean intersects(AABB aabb) {
		return aabb.maxX > this.minX && aabb.minX < this.maxX && aabb.maxY > this.minY && aabb.minY < this.maxY && aabb.maxZ > this.minZ && aabb.minZ < this.maxZ;
	}
	
	/**
	 * Sets bounds and returns itself
	 */
	public AABB set(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		return this;
	}
	
	/**
	 * Creates a new expanded bounding box 
	 */
	public AABB expand(float x, float y, float z) {
		float minX = this.minX;
		float minY = this.minY;
		float minZ = this.minZ;
		float maxX = this.maxX;
		float maxY = this.maxY;
		float maxZ = this.maxZ;
		
		if(x < 0) minX += x;
		else if(x > 0) maxX += x;
		
		if(y < 0) minY += y;
		else if(y > 0) maxY += y;
		
		if(z < 0) minZ += z;
		else if(z > 0) maxZ += z;
		
		return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public float clipXCollide(AABB aabb, float f) {
		if(aabb.maxY <= this.minY || aabb.minY >= this.maxY) return f;
		if(aabb.maxZ <= this.minZ || aabb.minZ >= this.maxZ) return f;
		
		if(f > 0 && aabb.maxX <= this.minX) {
			float ff = this.minX - aabb.maxX;
			if(ff < f) f = ff;
		}
		
		if(f < 0 && aabb.minX >= this.maxX) {
			float ff = this.maxX - aabb.minX;
			if(ff > f) f = ff;
		}
		return f;
	}
	
	public float clipYCollide(AABB aabb, float f) {
		if(aabb.maxX <= this.minX || aabb.minX >= this.maxX) return f;
		if(aabb.maxZ <= this.minZ || aabb.minZ >= this.maxZ) return f;
		
		if(f > 0 && aabb.maxY <= this.minY) {
			float ff = this.minY - aabb.maxY;
			if(ff < f) f = ff;
		}
		
		if(f < 0 && aabb.minY >= this.maxY) {
			float ff = this.maxY - aabb.minY;
			if(ff > f) f = ff;
		}
		return f;
	}
	
	public float clipZCollide(AABB aabb, float f) {
		if(aabb.maxX <= this.minX || aabb.minX >= this.maxX) return f;
		if(aabb.maxY <= this.minY || aabb.minY >= this.maxY) return f;
		
		if(f > 0 && aabb.maxZ <= this.minZ) {
			float ff = this.minZ - aabb.maxZ;
			if(ff < f) f = ff;
		}
		
		if(f < 0 && aabb.minZ >= this.maxZ) {
			float ff = this.maxZ - aabb.minZ;
			if(ff > f) f = ff;
		}
		return f;
	}

	/**
	 * Offsets and returns itself
	 */
	public AABB move(float x, float y, float z) {
		this.minX += x;
		this.minY += y;
		this.minZ += z;
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;
		
		return this;
	}
	
}
