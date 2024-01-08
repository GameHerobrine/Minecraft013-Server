package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class StartGamePacket extends MinecraftDataPacket{
	
	public float posX, posY, posZ;
	public int eid;
	public int seed;
	@Override
	public byte pid() {
		return ProtocolInfo.START_GAME_PACKET;
	}

	@Override
	public void decode() {
		
	}

	@Override
	public void encode() {
		this.putByte(pid());
		this.putInt(seed);
		this.putInt(eid);
		this.putInt(eid); //i wish to know wth is this
		this.putFloat(posX);
		this.putFloat(posY);
		this.putFloat(posZ);
	}
	
	@Override
	public int getSize() {
		return 1 + 4 + 4 + 4 + 4 + 4 + 4;
	}
}
