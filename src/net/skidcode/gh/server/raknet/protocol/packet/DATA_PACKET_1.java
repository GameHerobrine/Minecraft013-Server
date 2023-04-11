package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.protocol.DataPacket;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DATA_PACKET_1 extends DataPacket {
    public static byte ID = (byte) 0x81;

    @Override
    public byte getID() {
        return ID;
    }

}
