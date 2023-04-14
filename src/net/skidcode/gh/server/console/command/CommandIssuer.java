package net.skidcode.gh.server.console.command;

public interface CommandIssuer {
	public String getIssuerName();
	
	public void sendOutput(String s);
}
