package net.skidcode.gh.server.entity;

import java.util.ArrayList;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.utils.AABB;
import net.skidcode.gh.server.world.World;

public abstract class Entity {
	
	public float posX = 0, posY = 0, posZ = 0;
	public float yaw, pitch;
	public int eid;
	public World world;
	public AABB boundingBox = new AABB(-0.5f, 0, -0.5f, 0.5f, 1, 0.5f);
	public float width = 1, height = 1, radius = 0.5f;
	public float lastX, lastY, lastZ;
	public float motionX, motionY, motionZ;
	public boolean removed = false;
	public boolean onGround = false;
	
	public Entity() {
		this.setPosition(Server.world.spawnX, Server.world.spawnY, Server.world.spawnZ, 0, 0);
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
	
	public void move(float mx, float my, float mz) {
		//TODO better move
		float sx = mx;
		float sy = my;
		float sz = mz;
		
		ArrayList<AABB> cubes = this.world.getCubes(this, this.boundingBox.expand(mx, my, mz));
		for(int i = 0; i < cubes.size(); ++i) {
			my = cubes.get(i).clipYCollide(this.boundingBox, my);
		}
		this.boundingBox.move(0, my, 0);
		
		
		for(int i = 0; i < cubes.size(); ++i) {
			mx = cubes.get(i).clipXCollide(this.boundingBox, mx);
		}
		this.boundingBox.move(mx, 0, 0);
		
		
		for(int i = 0; i < cubes.size(); ++i) {
			mz = cubes.get(i).clipZCollide(this.boundingBox, mz);
		}
		this.boundingBox.move(0, 0, mz);
		
		this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2;
		this.posY = this.boundingBox.minY;
		this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2;
		
		this.onGround = my != sy && sy < 0;
		
	}
	
	public void remove() {
		this.removed = true;
	}
	
	public void tick() {
		//System.out.println(this.boundingBox);
	}
	
	public void setPosition(float x, float y, float z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.boundingBox.set(x - this.radius, y, z - this.radius, x + this.radius, y + this.height, z + this.radius);
	}
}
