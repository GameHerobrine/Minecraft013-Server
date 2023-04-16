package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class UpdateBlockPacket extends MinecraftDataPacket{
	
	public int posX, posZ;
	public byte posY, id, metadata;
	
	@Override
	public byte pid() {
		return ProtocolInfo.UPDATE_BLOCK_PACKET;
	}

	@Override
	public void decode() {
		this.posX = this.getInt();
		this.posZ = this.getInt();
		this.posY = this.getByte();
		this.id = this.getByte();
		this.metadata = this.getByte();
	}

	@Override
	public void encode() {
		this.putByte(pid());
		this.putInt(this.posX);
		this.putInt(this.posZ);
		this.putByte(this.posY);
		this.putByte(this.id);
		this.putByte(this.metadata);
	}

}
