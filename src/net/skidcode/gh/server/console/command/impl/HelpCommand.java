package net.skidcode.gh.server.console.command.impl;

import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.CommandIssuer;

public class HelpCommand extends CommandBase{

	public HelpCommand(String name) {
		super(name);
	}

	@Override
	public String processCommand(CommandIssuer issuer, String... parameters) {
		String s = "Availible commands:\n";
		for(CommandBase c : CommandBase.commands.values()) {
			s += c.name + "\n";
		}
		return s;
	}
	
}
