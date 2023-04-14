package net.skidcode.gh.server.console.command;

import java.util.HashMap;

import net.skidcode.gh.server.console.command.impl.BroadcastCommand;
import net.skidcode.gh.server.console.command.impl.TeleportCommand;

public abstract class CommandBase {
	
	public final String name;
	public CommandBase(String name) {
		this.name = name;
	}
	public abstract String processCommand(CommandIssuer issuer, String... parameters);
	
	
	public static HashMap<String, CommandBase> commands = new HashMap<>();
	
	public static void addCommand(CommandBase cmd) {
		commands.put(cmd.name, cmd);
	}
	
	
	static {
		addCommand(new BroadcastCommand("broadcast"));
		addCommand(new TeleportCommand("teleport"));
	}
	
}
