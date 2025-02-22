package net.skidcode.gh.server.event.player;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.event.Event;
import net.skidcode.gh.server.player.Player;

public class PlayerSendChatMessage extends Event{

	private static final long _id = Event.getNextFreeID();
	public final Player player;
	public String message;
	
	public PlayerSendChatMessage(Player player, String message) {
		this.player = player;
		this.message = message;
	}
	
	@Override
	public long getID() {
		return _id;
	}
}
