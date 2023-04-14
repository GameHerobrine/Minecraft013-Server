package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class RemoveBlockPacket extends MinecraftDataPacket{
	
	public int eid, posX, posZ;
	public byte posY;
	
	@Override
	public byte pid() {
		return ProtocolInfo.REMOVE_BLOCK_PACKET;
	}

	@Override
	public void decode() {
		this.eid = this.getInt();
		this.posX = this.getInt();
		this.posZ = this.getInt();
		this.posY = this.getByte();
	}

	@Override
	public void encode() {
		this.putByte(this.pid());
		this.putInt(this.eid);
		this.putInt(this.posX);
		this.putInt(this.posZ);
		this.putByte(this.posY);
	}

}
