package net.skidcode.gh.server.console.command.impl;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.CommandIssuer;
import net.skidcode.gh.server.network.protocol.MessagePacket;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Logger;

public class BroadcastCommand extends CommandBase{

	public BroadcastCommand(String name) {
		super(name);
	}

	@Override
	public String processCommand(CommandIssuer issuer, String... parameters) {
		String msg = String.join(" ", parameters);
		if(parameters.length <= 0) return "You must enter a message.";
		MessagePacket mp = new MessagePacket(); //TODO API for all kind of this stuff
		mp.message = "[Broadcast] "+msg;
		Logger.raw(mp.message);
		for(Player p : Server.getPlayers()) {
			p.dataPacket(mp);
		}
		
		return "";
	}
	
}
