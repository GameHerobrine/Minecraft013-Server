package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class RemoveEntityPacket extends MinecraftDataPacket{
	
	public int eid;
	
	@Override
	public byte pid() {
		return ProtocolInfo.REMOVE_ENTITY_PACKET;
	}

	@Override
	public void decode() {
		
	}

	@Override
	public void encode() {
		this.putByte(this.pid());
		this.putInt(this.eid);
	}
	
	@Override
	public int getSize() {
		return 1 + 4;
	}
}
