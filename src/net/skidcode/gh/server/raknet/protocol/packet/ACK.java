package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.raknet.protocol.AcknowledgePacket;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ACK extends AcknowledgePacket {

	public static byte ID = (byte) 0xc0;

	@Override
	public byte getID() {
		return ID;
	}
}
