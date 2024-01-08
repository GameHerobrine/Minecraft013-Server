package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class PlaceBlockPacket extends MinecraftDataPacket{

	public byte posY, face, id;
	public int eid, posX, posZ;
	@Override
	public byte pid() {
		return ProtocolInfo.PLACE_BLOCK_PACKET;
	}

	@Override
	public void decode() {
		this.eid = this.getInt();
		this.posX = this.getInt();
		this.posZ = this.getInt();
		
		this.posY = this.getByte();
		this.face = this.getByte();
		this.id = this.getByte();
	}

	@Override
	public void encode() {
		this.putByte(pid());
		this.putInt(this.eid);
		this.putInt(this.posX);
		this.putInt(this.posZ);
		this.putByte(this.posY);
		this.putByte(this.face);
		this.putByte(this.id);
	}
	
	@Override
	public int getSize() {
		return 1 + 4 + 4 + 4 + 1 + 1 + 1;
	}
}
