package net.skidcode.gh.server.player;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.entity.Entity;
import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;
import net.skidcode.gh.server.network.protocol.LoginPacket;
import net.skidcode.gh.server.network.protocol.StartGamePacket;
import net.skidcode.gh.server.protocol.MovePlayerPacket;
import net.skidcode.gh.server.utils.Logger;

public class Player extends Entity{
	
	public long clientID;
	public int port;
	public String ip, identifier, nickname;
	public Player(String identifier, long clientID, String ip, int port) {
		super();
		this.clientID = clientID;
		this.port = port;
		this.ip = ip;
		this.identifier = identifier;
	}
	
	public void dataPacket(MinecraftDataPacket pk) {
		Server.handler.sendPacket(this, pk);
	}
	
	public void handlePacket(MinecraftDataPacket dp) {
		switch(dp.pid()) {
			case ProtocolInfo.LOGIN_PACKET:
				LoginPacket loginpacket = (LoginPacket)dp;
				this.nickname = loginpacket.nickname;
				
				StartGamePacket pk = new StartGamePacket();
				pk.posX = this.posX;
				pk.posY = this.posY;
				pk.posZ = this.posZ;
				this.dataPacket(pk);
				break;
			case ProtocolInfo.MOVE_PLAYER_PACKET_PACKET:
				MovePlayerPacket moveplayerpacket = (MovePlayerPacket)dp;
				this.posX = moveplayerpacket.posX;
				this.posY = moveplayerpacket.posY;
				this.posZ = moveplayerpacket.posZ;
				this.pitch = moveplayerpacket.pitch;
				this.yaw = moveplayerpacket.yaw;
				break;
			default:
				Logger.warn("Unknown PID: "+dp.pid());
				break;
		}
	}
}
