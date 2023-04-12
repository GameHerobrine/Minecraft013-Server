package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class PlaceBlockPacket extends MinecraftDataPacket{

	public byte posY, unknown5, id;
	public int unknown1, posX, posZ;
	@Override
	public byte pid() {
		return ProtocolInfo.PLACE_BLOCK_PACKET;
	}

	@Override
	public void decode() {
		this.unknown1 = this.getInt();
		this.posX = this.getInt();
		this.posZ = this.getInt();
		
		this.posY = this.getByte();
		this.unknown5 = this.getByte();
		this.id = this.getByte();
	}

	@Override
	public void encode() {
		this.putByte(pid());
		this.putInt(this.unknown1);
		this.putInt(this.posX);
		this.putInt(this.posZ);
		this.putByte(this.posY);
		this.putByte(this.unknown5);
		this.putByte(this.id);
	}

}
