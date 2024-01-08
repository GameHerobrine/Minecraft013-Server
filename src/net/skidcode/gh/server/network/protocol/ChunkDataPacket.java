package net.skidcode.gh.server.network.protocol;

import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;

public class ChunkDataPacket extends MinecraftDataPacket{
	
	public int chunkX, chunkZ;
	public byte[] data;
	@Override
	public byte pid() {
		return ProtocolInfo.CHUNK_DATA_PACKET;
	}

	@Override
	public void decode() {
		
	}

	@Override
	public void encode() {
		this.putByte(pid());
		this.putInt(this.chunkX);
		this.putInt(this.chunkZ);
		this.put(data);
	}
	
	@Override
	public int getSize() {
		return 1 + 4 + 4 + data.length;
	}
}
