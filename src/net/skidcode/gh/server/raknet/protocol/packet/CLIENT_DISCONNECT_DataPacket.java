package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.raknet.protocol.Packet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class CLIENT_DISCONNECT_DataPacket extends Packet {
    public static byte ID = (byte) 0x15;

    @Override
    public byte getID() {
        return ID;
    }
}
