package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class RequestChunkPacket extends MinecraftDataPacket{

	public int chunkX, chunkZ;
	
	@Override
	public byte pid() {
		return ProtocolInfo.REQUEST_CHUNK_PACKET;
	}

	@Override
	public void decode() {
		this.chunkX = this.getInt();
		this.chunkZ = this.getInt();
	}

	@Override
	public void encode() {
		this.putByte(this.pid());
		this.putInt(this.chunkX);
		this.putInt(this.chunkZ);
	}
	
	@Override
	public int getSize() {
		return 1 + 4 + 4;
	}
}
