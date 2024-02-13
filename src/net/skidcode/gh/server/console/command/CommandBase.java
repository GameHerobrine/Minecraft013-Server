package net.skidcode.gh.server.console.command;

import java.util.HashMap;

import net.skidcode.gh.server.console.command.impl.BroadcastCommand;
import net.skidcode.gh.server.console.command.impl.FillChunkCommand;
import net.skidcode.gh.server.console.command.impl.HelpCommand;
import net.skidcode.gh.server.console.command.impl.PlayerListCommand;
import net.skidcode.gh.server.console.command.impl.StopCommand;
import net.skidcode.gh.server.console.command.impl.TeleportCommand;
import net.skidcode.gh.server.console.command.impl.TpsCommand;

public abstract class CommandBase {
	
	public final String name;
	public final String description;
	
	public CommandBase(String name) {
		this(name, "[...]");
	}
	
	public CommandBase(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public abstract String processCommand(CommandIssuer issuer, String... parameters);
	
	
	public static HashMap<String, CommandBase> commands = new HashMap<>();
	
	public static void addCommand(CommandBase cmd) {
		commands.put(cmd.name, cmd);
	}
	
	static {
		addCommand(new HelpCommand("help", ""));
		addCommand(new BroadcastCommand("broadcast", "<msg>"));
		addCommand(new TeleportCommand("teleport", "<x> <y> <z> <playername>"));
		addCommand(new StopCommand("stop", ""));
		addCommand(new FillChunkCommand("fillchunk", "wth_are_u_doing"));
		addCommand(new TpsCommand("tps", ""));
		addCommand(new PlayerListCommand("playerlist", ""));
	}
	
}
