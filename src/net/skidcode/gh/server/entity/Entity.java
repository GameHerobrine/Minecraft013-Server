package net.skidcode.gh.server.entity;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.world.World;

public abstract class Entity {
	
	public float posX, posY, posZ;
	public float yaw, pitch;
	public int eid;
	public World world;
	public Entity() { //TODO later
		this.posX = 64;
		this.posY = 128;
		this.posZ = 64;
		this.yaw = 0;
		this.pitch = 0;
		this.world = Server.world;
		this.eid = world.incrementAndGetNextFreeEID();
	}
}
