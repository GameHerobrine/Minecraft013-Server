package net.skidcode.gh.server.entity;

import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.MathUtils;
import net.skidcode.gh.server.world.World;

public class PrimedTnt extends Entity{
	
	public int ticksUntilExplosion = 0;
	
	public PrimedTnt(World world, float x, float y, float z) {
		this.setSize(0.98f, 0.98f);
		float f = MathUtils.random();
		float f1 = (f * 3.1416f) + (f * 3.1416f);
		
		this.motionX = MathUtils.sin(0.017453f * f1) * -0.02f;
		this.motionY = 0.2f;
		this.motionZ = MathUtils.cos(0.017453f * f1) * -0.02f;
		
		this.setPosition(x, y, z);
		this.ticksUntilExplosion = 80;
		this.lastX = x;
		this.lastY = y;
		this.lastZ = z;
	}
	
	public void tick() {
		this.lastX = this.posX;
		this.lastY = this.posY;
		this.lastZ = this.posZ;
		
		this.motionY -= 0.04f;
		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.98f;
		this.motionY *= 0.98f;
		this.motionZ *= 0.98f;


		--this.ticksUntilExplosion;
		if(this.ticksUntilExplosion > 0) {
			
		}else {
			this.remove();
			this.explode();
		}
	}

	public void explode() {
		this.world.explode(this, this.posX, this.posY, this.posZ, 2.5f); //tnt is less powerful in 0.1.3
	}
}
