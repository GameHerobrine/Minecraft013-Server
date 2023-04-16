package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.raknet.protocol.DataPacket;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class DATA_PACKET_6 extends DataPacket {
    public static byte ID = (byte) 0x86;

    @Override
    public byte getID() {
        return ID;
    }

}
