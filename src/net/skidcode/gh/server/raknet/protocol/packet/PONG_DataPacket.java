package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.protocol.Packet;

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

    public long pingID;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.pingID);
    }

    @Override
    public void decode() {
        super.decode();
        this.pingID = this.getLong();
    }
}
