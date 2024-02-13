package net.skidcode.gh.server.console.command.impl;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.CommandIssuer;

public class TpsCommand extends CommandBase{

	public TpsCommand(String name, String desc) {
		super(name, desc);
	}

	@Override
	public String processCommand(CommandIssuer issuer, String... parameters) {
		return Server.stableTPS+" tps";
	}

}
