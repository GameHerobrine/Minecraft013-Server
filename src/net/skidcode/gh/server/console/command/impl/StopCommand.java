package net.skidcode.gh.server.console.command.impl;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.CommandIssuer;

public class StopCommand extends CommandBase{

	public StopCommand(String name) {
		super(name);
	}

	@Override
	public String processCommand(CommandIssuer issuer, String... parameters) {
		Server.running = false;
		Server.handler.notifyShutdown();
		return "Stopping server...";
	}
	
}
