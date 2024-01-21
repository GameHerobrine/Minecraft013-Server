package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class PING_DataPacket extends Packet {
	public static byte ID = (byte) 0x00;

	@Override
	public byte getID() {
		return ID;
	}

	public long pingTime;

	@Override
	public void encode() {
		super.encode();
		this.putLong(this.pingTime);
	}

	@Override
	public void decode() {
		super.decode();
		this.pingTime = this.getLong();
	}
}
