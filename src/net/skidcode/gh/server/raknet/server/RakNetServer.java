package net.skidcode.gh.server.raknet.server;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.skidcode.gh.server.utils.Logger;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class RakNetServer extends Thread {
	protected int port;
	protected String interfaz;
	public SessionManager sessionManager;
	protected ConcurrentLinkedQueue<byte[]> externalQueue;
	protected ConcurrentLinkedQueue<byte[]> internalQueue;

	protected boolean shutdown;


	public RakNetServer(int port) {
		this(port, "0.0.0.0");
	}

	public RakNetServer(int port, String interfaz) {
		this.port = port;
		if (port < 1 || port > 65536) {
			throw new IllegalArgumentException("Invalid port range");
		}

		this.interfaz = interfaz;

		this.externalQueue = new ConcurrentLinkedQueue<>();
		this.internalQueue = new ConcurrentLinkedQueue<>();

		this.start();
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public void shutdown() {
		this.shutdown = true;
	}

	public int getPort() {
		return port;
	}

	public String getInterface() {
		return interfaz;
	}

	public ConcurrentLinkedQueue<byte[]> getExternalQueue() {
		return externalQueue;
	}

	public ConcurrentLinkedQueue<byte[]> getInternalQueue() {
		return internalQueue;
	}

	public void pushMainToThreadPacket(byte[] data) {
		this.internalQueue.add(data);
	}

	public byte[] readMainToThreadPacket() {
		return this.internalQueue.poll();
	}

	public void pushThreadToMainPacket(byte[] data) {
		this.externalQueue.add(data);
	}

	public byte[] readThreadToMainPacket() {
		return this.externalQueue.poll();
	}

	private class ShutdownHandler extends Thread {
		public void run() {
			if (!shutdown) {
				Logger.error("RakNet crashed!");
			}
		}
	}

	@Override
	public void run() {
		this.setName("RakNet Thread #" + Thread.currentThread().getId());
		Runtime.getRuntime().addShutdownHook(new ShutdownHandler());
		UDPServerSocket socket = new UDPServerSocket(port, this.interfaz);
		try {
			this.sessionManager = new SessionManager(this, socket);
			this.sessionManager.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
