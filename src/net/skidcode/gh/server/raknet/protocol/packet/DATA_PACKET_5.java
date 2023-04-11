package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.protocol.DataPacket;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DATA_PACKET_5 extends DataPacket {
    public static byte ID = (byte) 0x85;

    @Override
    public byte getID() {
        return ID;
    }

}
