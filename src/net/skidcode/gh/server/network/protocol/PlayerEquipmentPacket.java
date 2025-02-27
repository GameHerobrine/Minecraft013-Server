package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.PacketWithEID;
import net.skidcode.gh.server.network.ProtocolInfo;

public class PlayerEquipmentPacket extends MinecraftDataPacket implements PacketWithEID{
	
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
	public int getEID() {
		return this.eid;
	}

	@Override
	public void setEID(int eid) {
		this.eid = eid;
	}
	
	@Override
	public void encode() {
		this.putByte(pid());
		this.putInt(this.eid);
		this.putByte(this.itemID);
	}
	
	@Override
	public int getSize() {
		return 1 + 4 + 1;
	}
}
