package net.skidcode.gh.server.raknet.protocol.packet;

import net.skidcode.gh.server.protocol.Packet;
import net.skidcode.gh.server.raknet.RakNet;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class UNCONNECTED_PING extends Packet {
    public static byte ID = (byte) 0x01;

    @Override
    public byte getID() {
        return ID;
    }

    public long pingID;

    @Override
    public void encode() {
        super.encode();
        this.putLong(this.pingID);
        this.put(RakNet.MAGIC);
    }

    @Override
    public void decode() {
        super.decode();
        this.pingID = this.getLong();
    }
}
