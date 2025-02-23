package net.skidcode.gh.server.console.command.impl;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.CommandIssuer;
import net.skidcode.gh.server.network.protocol.MessagePacket;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Logger;

public class BroadcastCommand extends CommandBase{

	public BroadcastCommand(String name, String desc) {
		super(name, desc);
	}

	@Override
	public String processCommand(CommandIssuer issuer, String... parameters) {
		String msg = String.join(" ", parameters);
		if(parameters.length <= 0) return "You must enter a message.";
		
		String message = "[Broadcast] "+msg;
		Server.broadcastMessage(message, true);
		return "";
	}
	
}
