package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class PlayerEquipmentPacket extends MinecraftDataPacket{
	
	public int eid;
	public byte itemID;
	
	@Override
	public byte pid() {
		return ProtocolInfo.PLAYER_EQUIPMENT_PACKET;
	}

	@Override
	public void decode() {
		this.eid = this.getInt();
		this.itemID = this.getByte();
	}

	@Override
	public void encode() {
		this.putByte(pid());
		this.putInt(this.eid);
		this.putByte(this.itemID);
	}

}
