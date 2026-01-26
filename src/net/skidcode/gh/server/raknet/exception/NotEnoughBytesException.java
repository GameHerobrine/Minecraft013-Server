package net.skidcode.gh.server.raknet.exception;

public class NotEnoughBytesException extends RakNetException{
	private static final long serialVersionUID = -8493273385324196335L;

	public NotEnoughBytesException(int need, int got) {
		super("Not enough bytes: needed "+need+", got "+got+"!");
	}
}
