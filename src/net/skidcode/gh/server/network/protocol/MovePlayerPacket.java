package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class MovePlayerPacket extends MinecraftDataPacket{
	
	public int eid;
	public float posX, posY, posZ, pitch, yaw;
	
	@Override
	public byte pid() {
		return ProtocolInfo.MOVE_PLAYER_PACKET_PACKET;
	}

	@Override
	public void decode() {
		this.eid = this.getInt();
		this.posX = this.getFloat();
		this.posY = this.getFloat();
		this.posZ = this.getFloat();
		this.pitch = this.getFloat();
		this.yaw = this.getFloat();
	}

	@Override
	public void encode() {
		this.putByte(pid());
		this.putInt(this.eid);
		this.putFloat(this.posX);
		this.putFloat(this.posY);
		this.putFloat(this.posZ);
		this.putFloat(this.pitch);
		this.putFloat(this.yaw);
	}

	@Override
	public int getSize() {
		return 1 + 4 + 4 + 4 + 4 + 4 + 4;
	}
	
}
