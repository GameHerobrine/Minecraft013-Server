package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class AddPlayerPacket extends MinecraftDataPacket{
	
	public long clientID;
	public String nickname;
	public int eid;
	
	public float posX, posY, posZ;
	
	@Override
	public byte pid() {
		return ProtocolInfo.ADD_PLAYER_PACKET;
	}

	@Override
	public void decode() {
		
	}
	
	@Override
	public void encode() {
		this.putByte(pid());
		this.putLong(this.clientID);
		this.putString(this.nickname);
		this.putInt(this.eid);
		this.putFloat(this.posX);
		this.putFloat(this.posY);
		this.putFloat(this.posZ);
	}

	@Override
	public int getSize() {
		return 1 + 8 + 2+this.nickname.length() + 4 + 4 + 4 + 4;
	}

}
