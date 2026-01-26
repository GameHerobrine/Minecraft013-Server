package net.skidcode.gh.server.raknet.protocol;


import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import net.skidcode.gh.server.raknet.exception.NotEnoughBytesException;
import net.skidcode.gh.server.raknet.exception.RakNetException;
import net.skidcode.gh.server.utils.Binary;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class Packet implements Cloneable {
	
	protected int offset = 0;
	public byte[] buffer;
	public Long sendTime;

	public abstract byte getID();
	
	protected byte[] get(int len) throws RakNetException {
		if (len < 0) {
			this.offset = this.buffer.length - 1;
			return new byte[0];
		}
		this.require(len);
		byte[] buffer = new byte[len];
		for (int i = 0; i < len; i++) {
			buffer[i] = this.buffer[this.offset++];
		}
		return buffer;
	}

	protected byte[] getAll() {
		return this.get();
	}

	protected byte[] get() {
		return Arrays.copyOfRange(this.buffer, this.offset, this.buffer.length - 1);
	}

	protected long getLong() throws RakNetException{
		return Binary.readLong(this.get(8));
	}

	protected int getInt() throws RakNetException{
		return Binary.readInt(this.get(4));
	}

	protected short getSignedShort() throws RakNetException {
		return (short) this.getShort();
	}

	protected int getShort() throws RakNetException{
		return Binary.readShort(this.get(2));
	}

	protected int getTriad() throws RakNetException{
		return Binary.readTriad(this.get(3));
	}

	protected int getLTriad() throws RakNetException{
		return Binary.readLTriad(this.get(3));
	}

	protected byte getByte() throws RakNetException {
		this.require(1);
		return this.buffer[this.offset++];
	}

	protected String getString() throws RakNetException{
		return new String(this.get(this.getSignedShort()), StandardCharsets.UTF_8);
	}

	protected InetSocketAddress getAddress() throws RakNetException{
		byte version = this.getByte();
		if (version == 4) {
			String addr = ((~this.getByte()) & 0xff) + "." + ((~this.getByte()) & 0xff) + "." + ((~this.getByte()) & 0xff) + "." + ((~this.getByte()) & 0xff);
			int port = this.getSignedShort() & 0xffff;
			return new InetSocketAddress(addr, port);
		} else {
			throw new RakNetException("unsupported address version: "+version);
			//todo IPV6 SUPPORT
		}
	}

	protected boolean feof() {
		return !(this.offset >= 0 && this.offset + 1 <= this.buffer.length);
	}

	protected void put(byte[] b) {
		this.buffer = Binary.appendBytes(this.buffer, b);
	}

	protected void putLong(long v) {
		this.put(Binary.writeLong(v));
	}

	protected void putInt(int v) {
		this.put(Binary.writeInt(v));
	}

	protected void putShort(int v) {
		this.put(Binary.writeShort(v));
	}

	protected void putSignedShort(short v) {
		this.put(Binary.writeShort(v & 0xffff));
	}

	protected void putTriad(int v) {
		this.put(Binary.writeTriad(v));
	}

	protected void putLTriad(int v) {
		this.put(Binary.writeLTriad(v));
	}

	protected void putByte(byte b) {
		byte[] newBytes = new byte[this.buffer.length + 1];
		System.arraycopy(this.buffer, 0, newBytes, 0, this.buffer.length);
		newBytes[this.buffer.length] = b;
		this.buffer = newBytes;
	}

	protected void putString(String str) {
		byte[] b = str.getBytes(StandardCharsets.UTF_8);
		this.putShort(b.length);
		this.put(b);
	}

	protected void putAddress(String addr, int port) {
		this.putAddress(addr, port, (byte) 4);
	}

	protected void putAddress(String addr, int port, byte version) {
		this.putByte(version);
		if (version == 4) {
			for (String b : addr.split("\\.")) {
				this.putByte((byte) ((~Integer.valueOf(b)) & 0xff));
			}
			this.putShort(port);
		} else {
			//TODO ipv6
		}
	}

	protected void putAddress(InetSocketAddress address) {
		this.putAddress(address.getHostString(), address.getPort());
	}

	public void encode() {
		this.buffer = new byte[]{getID()};
	}

	public void decode() throws RakNetException {
		this.offset = 1;
	}

	public Packet clean() {
		this.buffer = null;
		this.offset = 0;
		this.sendTime = null;
		return this;
	}

	@Override
	public Packet clone() throws CloneNotSupportedException {
		Packet packet = (Packet) super.clone();
		packet.buffer = this.buffer.clone();
		return packet;
	}
	
	public void require(int n) throws NotEnoughBytesException{
		int needed = (n + this.offset);
		int got = this.buffer.length;
		if(needed > got) {
			throw new NotEnoughBytesException(needed, got);
		}
	}
}
