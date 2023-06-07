package net.skidcode.gh.server.plugin;

public abstract class Plugin{
	
	public abstract void onEnable();
	
	public void onServerInitialized() {}
	
	public void onDisable() {}
}
