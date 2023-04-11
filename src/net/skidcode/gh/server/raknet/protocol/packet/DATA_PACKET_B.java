package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.protocol.DataPacket;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DATA_PACKET_B extends DataPacket {
    public static byte ID = (byte) 0x8b;

    @Override
    public byte getID() {
        return ID;
    }

}
