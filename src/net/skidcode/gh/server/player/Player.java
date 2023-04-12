package net.skidcode.gh.server.player;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.entity.Entity;
import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;
import net.skidcode.gh.server.network.protocol.AddPlayerPacket;
import net.skidcode.gh.server.network.protocol.LoginPacket;
import net.skidcode.gh.server.network.protocol.MovePlayerPacket;
import net.skidcode.gh.server.network.protocol.PlaceBlockPacket;
import net.skidcode.gh.server.network.protocol.StartGamePacket;
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
				this.world.addPlayer(this);
				StartGamePacket pk = new StartGamePacket();
				pk.seed = 0;
				pk.eid = this.eid;
				pk.posX = this.posX;
				pk.posY = this.posY;
				pk.posZ = this.posZ;
				this.dataPacket(pk);
				
				for(Player player : this.world.players.values()) {
					if(player.eid != this.eid) { //TODO move to World::addPlayer ?
						AddPlayerPacket pkk = new AddPlayerPacket();
						pkk.clientID = player.clientID;
						pkk.eid = player.eid;
						pkk.nickname = player.nickname;
						pkk.posX = player.posX;
						pkk.posY = player.posY;
						pkk.posZ = player.posZ;
						this.dataPacket(pkk);
						
						AddPlayerPacket pkk2 = new AddPlayerPacket();
						pkk2.clientID = this.clientID;
						pkk2.eid = this.eid;
						pkk2.nickname = this.nickname;
						pkk2.posX = this.posX;
						pkk2.posY = this.posY;
						pkk2.posZ = this.posZ;
						player.dataPacket(pkk2);
					}
				}
				
				break;
			case ProtocolInfo.PLACE_BLOCK_PACKET:
				PlaceBlockPacket pbp = (PlaceBlockPacket) dp;
				this.world.placeBlock(pbp.posX, pbp.posY, pbp.posZ, pbp.id, this);
				break;
			case ProtocolInfo.MOVE_PLAYER_PACKET_PACKET: //TODO send updates
				MovePlayerPacket moveplayerpacket = (MovePlayerPacket)dp;
				this.posX = moveplayerpacket.posX;
				this.posY = moveplayerpacket.posY;
				this.posZ = moveplayerpacket.posZ;
				this.pitch = moveplayerpacket.pitch;
				this.yaw = moveplayerpacket.yaw;
				
				moveplayerpacket.eid = this.eid;
				moveplayerpacket.setBuffer(new byte[] {});
				this.world.broadcastPacketFromPlayer(moveplayerpacket, this);
				
				break;
			default:
				Logger.warn("Unknown PID: "+dp.pid());
				break;
		}
	}
}
