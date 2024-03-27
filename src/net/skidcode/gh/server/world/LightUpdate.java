package net.skidcode.gh.server.world;

public class LightUpdate {
	public LightLayer layer;
	public int minX, minY, minZ;
	public int maxX, maxY, maxZ;
	public LightUpdate(LightLayer layer, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.layer = layer;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	public boolean expandToContain(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if(this.minX <= minX && this.minY <= minY && this.minZ <= minZ && this.maxX >= maxX && this.maxY >= maxY && this.maxZ >= maxZ) {
			return true; //already updated
		}
		
		if(this.minX - 1 > minX || this.minY - 1 > minZ || this.minZ - 1 > minZ || this.maxX + 1 < maxX || this.maxY + 1 < maxY || this.maxZ + 1 < maxZ) {
			return false;
		}

		if(this.minX < minX) minX = this.minX;
		if(this.minY < minY) minY = this.minY;
		if(this.minZ < minZ) minZ = this.minZ;
		
		if(this.maxX > maxX) maxX = this.maxX;
		if(this.maxY > maxY) maxY = this.maxY;
		if(this.maxZ > maxZ) maxZ = this.maxZ;
		
		if((maxZ - minZ)*(maxY-minY)*(maxX-minX) - (this.maxX - this.minX)*(this.maxZ - this.minZ)*(this.maxY - this.minY) > 2) {
			return false;
		}
		
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
		
		return true;
	}
}
