package net.skidcode.gh.server.world.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.world.data.RawBytesFile;

public class PlayerData extends RawBytesFile{
	
	public final Player player;
	private static final byte PLAYER_DATA_VERSION = 1;
	private byte fileVersion = PLAYER_DATA_VERSION;
	public PlayerData(Player player) throws IOException {
		super("world/players/"+player.nickname+".dat", 0);
		this.player = player;
		if(!this.created && Server.savePlayerData) {
			Logger.info("Creating playerdata for "+this.player.nickname);
			this.player.setPosition(Server.world.spawnX, Server.world.spawnY, Server.world.spawnZ);
			Files.createFile(Paths.get(this.filename));
			this.save();
		}
	}

	@Override
	public void parse() throws IOException {
		if(!Server.savePlayerData) return;
		this.fileVersion = this.getByte();
		if(this.fileVersion == PLAYER_DATA_VERSION) {
			this.player.setPosition(this.getFloat(), this.getFloat(), this.getFloat());
		}else {
			Logger.warn(player.nickname+"'s data has different version, aborting reading...");
		}
		
	}

	@Override
	public void save() throws IOException {
		if(!Server.savePlayerData) return;
		if(this.fileVersion == PLAYER_DATA_VERSION) {
			this.putByte(PLAYER_DATA_VERSION);
			this.putFloat(this.player.posX);
			this.putFloat(this.player.posY);
			this.putFloat(this.player.posZ);
			Files.write(Paths.get(this.filename), this.buffer);
		}else {
			Logger.warn(player.nickname+"'s data has different version, aborting writing...");
		}
		
	}
	
}
