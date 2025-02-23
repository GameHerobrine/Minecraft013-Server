package net.skidcode.gh.server.world;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.utils.Logger;

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
	
	@Override
	public String toString() {
		return String.format("layer %s min3 max3 %d %d %d %d %d %d", this.layer.name(), this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
	}
	
	public boolean expandToContain(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if(this.minX <= minX && this.minY <= minY && this.minZ <= minZ && this.maxX >= maxX && this.maxY >= maxY && this.maxZ >= maxZ) {
			return true; //already updated
		}
		
		if(this.minX - 1 > minX || this.minY - 1 > minY || this.minZ - 1 > minZ || this.maxX + 1 < maxX || this.maxY + 1 < maxY || this.maxZ + 1 < maxZ) {
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

	public void update(World world) {
		if((this.maxZ - this.minZ + 1)*(this.maxY-this.minY + 1)*(this.maxX-this.minX+1) > 0x8000) {
			return;
		}
		
		for(int x = this.minX; x <= this.maxX; ++x) {
			for(int z = this.minZ; z <= this.maxZ; ++z) {
				boolean hasChunks = world.hasChunksAt(x, 0, z, 1);
				
				if(hasChunks) {
					//also checks if world.getChunk(x >> 4, z >> 4) is empty, assuming it is never empty
				}
				
				if(hasChunks) {
					if(this.minY < 0) this.minY = 0;
					if(this.maxY > 127) this.maxY = 127;
					
					for(int y = this.minY; this.maxY >= y; ++y) {
						int brightness = world.getBrightness(this.layer, x, y, z);
						int blockID = world.getBlockIDAt(x, y, z);
						int lightBlock = Block.lightBlock[blockID];
						int newBrightness;
						if(lightBlock == 0) lightBlock = 1;
						int lightEmission = 0;
						if(this.layer == LightLayer.SKY) {
							if(world.isSkyLit(x, y, z)) lightEmission = 15;
						}else if(this.layer == LightLayer.BLOCK) {
							lightEmission = Block.lightEmission[blockID];
						}
						if(lightBlock <= 14 || lightEmission != 0) {
							int xNegBright = world.getBrightness(layer, x - 1, y, z);
							int xPosBright = world.getBrightness(layer, x + 1, y, z);
							int yNegBright = world.getBrightness(layer, x, y - 1, z);
							int yPosBright = world.getBrightness(layer, x, y + 1, z);
							int zNegBright = world.getBrightness(layer, x, y, z - 1);
							int zPosBright = world.getBrightness(layer, x, y, z + 1);
							
							int v15 = xNegBright;
							if(xPosBright > v15) v15 = xPosBright;
							if(yNegBright > v15) v15 = yNegBright;
							if(yPosBright > v15) v15 = yPosBright;
							if(zNegBright > v15) v15 = zNegBright;
							if(zPosBright > v15) v15 = zPosBright;
							
							newBrightness = v15 - lightBlock;
							if(newBrightness < 0) newBrightness = 0;
							if(lightEmission > newBrightness) newBrightness = lightEmission;
						}else {
							newBrightness = 0;
						}
						
						if(brightness != newBrightness) {
							world.setBrightness(this.layer, x, y, z, newBrightness);
							int v4 = newBrightness - 1;
							if(v4 < 0) v4 = 0;
							
							world.updateLightIfOtherThan(this.layer, x - 1, y, z, v4);
							world.updateLightIfOtherThan(this.layer, x, y - 1, z, v4);
							world.updateLightIfOtherThan(this.layer, x, y, z - 1, v4);
							
							if(x + 1 >= this.maxX) world.updateLightIfOtherThan(this.layer, x + 1, y, z, v4);
							if(y + 1 >= this.maxY) world.updateLightIfOtherThan(this.layer, x, y + 1, z, v4);
							if(z + 1 >= this.maxZ) world.updateLightIfOtherThan(this.layer, x, y, z + 1, v4);
						}
					}
					
				}
				
			}
		}
		
	}
}
