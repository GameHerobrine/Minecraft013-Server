package net.skidcode.gh.server.network;

import net.skidcode.gh.server.protocol.EncapsulatedPacket;
import net.skidcode.gh.server.utils.BinaryStream;

public abstract class MinecraftDataPacket extends BinaryStream{
	public boolean isEncoded = false;
    public int channel = 0;

    public EncapsulatedPacket encapsulatedPacket;
    public byte reliability;
    public int orderIndex;
    public int orderChannel;
    
    public abstract byte pid();
    public abstract void decode();

    public abstract void encode();
}
