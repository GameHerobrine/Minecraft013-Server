package net.skidcode.gh.server.console.command.impl;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.CommandIssuer;
import net.skidcode.gh.server.world.chunk.Chunk;

public class FillChunkCommand extends CommandBase{

	public FillChunkCommand(String name) {
		super(name);
	}

	@Override
	public String processCommand(CommandIssuer issuer, String... parameters) {
		if(parameters.length != 4) return "Usage: /fillchunk <x> <z> <id> <meta>";
		int x = Integer.parseInt(parameters[0]);
		int z = Integer.parseInt(parameters[1]);
		int id = Integer.parseInt(parameters[2]);
		int meta = Integer.parseInt(parameters[3]);
		Chunk c = Server.world.chunks[x][z];
		
		for(int blockX = 0; blockX < 16; ++blockX) {
			for(int blockZ = 0; blockZ < 16; ++blockZ) {
				for(int blockY = 0; blockY < 128; ++blockY) {
					c.blockData[blockX][blockZ][blockY] = (byte) id;
					c.blockMetadata[blockX][blockZ][blockY] = (byte) (meta);
				}
			}
		}
		
		
		return "";
	}
	
}
