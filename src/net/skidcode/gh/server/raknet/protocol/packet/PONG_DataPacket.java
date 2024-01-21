package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class PONG_DataPacket extends Packet {
	public static byte ID = (byte) 0x03;

	@Override
	public byte getID() {
		return ID;
	}

	public long pingTime;
	public long pongTime = 0;
	@Override
	public void encode() {
		super.encode();
		this.putLong(this.pingTime);
		this.putLong(this.pongTime);
	}

	@Override
	public void decode() {
		super.decode();
		this.pingTime = this.getLong();
		this.pongTime = this.getLong();
	}
}
