package net.skidcode.gh.server.console.command.impl;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.CommandIssuer;
import net.skidcode.gh.server.player.Player;

public class PlayerListCommand extends CommandBase {

	public PlayerListCommand(String name) {
		super(name);
	}

	@Override
	public String processCommand(CommandIssuer issuer, String... parameters) {
		String msg = "There are "+Server.getPlayers().size()+" players online:\n";
		for(Player p : Server.getPlayers()){
			msg+=p.nickname+", ";
		}
		return msg;
	}

}
