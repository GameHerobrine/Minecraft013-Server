package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.raknet.protocol.DataPacket;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DATA_PACKET_C extends DataPacket {
	public static byte ID = (byte) 0x8c;

	@Override
	public byte getID() {
		return ID;
	}
}
