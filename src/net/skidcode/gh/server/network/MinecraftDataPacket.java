package net.skidcode.gh.server.network;

import net.skidcode.gh.server.raknet.protocol.EncapsulatedPacket;
import net.skidcode.gh.server.utils.BinaryStream;

public abstract class MinecraftDataPacket extends BinaryStream implements Cloneable{
	public boolean isEncoded = false;
    public int channel = 0;

    public EncapsulatedPacket encapsulatedPacket;
    public byte reliability;
    public int orderIndex;
    public int orderChannel;
    
    public abstract byte pid();
    public abstract void decode();

    public abstract void encode();
    public abstract int getSize();
    @Override
    public MinecraftDataPacket clone() {
    	try {
			return (MinecraftDataPacket) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
    }
}
