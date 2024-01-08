package net.skidcode.gh.server.entity;

import net.skidcode.gh.server.utils.AxisAlignedBB;
import net.skidcode.gh.server.world.World;

public abstract class Entity {
	
	public float posX = 0, posY = 0, posZ = 0;
	public float yaw, pitch;
	public int eid;
	public World world;
	public AxisAlignedBB boundingBox = new AxisAlignedBB(-0.5f, 0, -0.5f, 0.5f, 1, 0.5f);
	public float width = 1, height = 1, radius = 0.5f;
	public Entity() {
		this.setPosition(128, 64, 128, 0, 0);
		this.eid = World.incrementAndGetNextFreeEID(); //TODO move to entity?
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		this.radius = this.width / 2;
	}
	
	public void setPosition(float x, float y, float z, float yaw, float pitch) {
		this.setPosition(x, y, z);
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public void tick() {
		//System.out.println(this.boundingBox);
	}
	
	public void setPosition(float x, float y, float z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.boundingBox.setBounds(x - this.radius, y, z - this.radius, x + this.radius, y + this.height, z + this.radius);
	}
}
