package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class LoginPacket extends MinecraftDataPacket{

	public String nickname;
	@Override
	public byte pid() {
		return ProtocolInfo.LOGIN_PACKET;
	}

	@Override
	public void decode() {
		nickname = this.getString();
	}

	@Override
	public void encode() {
		this.putByte(pid());
		this.putString(nickname);
	}
	
	@Override
	public int getSize() {
		return 1 + 2+this.nickname.length();
	}
}
