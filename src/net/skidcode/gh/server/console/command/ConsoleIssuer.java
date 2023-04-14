package net.skidcode.gh.server.console.command;

import net.skidcode.gh.server.utils.Logger;

public final class ConsoleIssuer implements CommandIssuer{
	public static final ConsoleIssuer INSTANCE = new ConsoleIssuer();
	@Override
	public String getIssuerName() {
		return "Console";
	}
	@Override
	public void sendOutput(String s) {
		Logger.cmd(s);
	}

}
