package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class MessagePacket extends MinecraftDataPacket{
	
	public String message;
	
	@Override
	public byte pid() {
		return ProtocolInfo.MESSAGE_PACKET;
	}

	@Override
	public void decode() {
		this.message = this.getString();
	}

	@Override
	public void encode() {
		this.putByte(pid());
		this.putString(this.message);
	}

	@Override
	public int getSize() {
		return 1 + 2+this.message.length();
	}
}
