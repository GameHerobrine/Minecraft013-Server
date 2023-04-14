package net.skidcode.gh.server.player;

import java.util.Arrays;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.console.command.CommandIssuer;
import net.skidcode.gh.server.entity.Entity;
import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;
import net.skidcode.gh.server.network.protocol.AddPlayerPacket;
import net.skidcode.gh.server.network.protocol.ChunkDataPacket;
import net.skidcode.gh.server.network.protocol.LoginPacket;
import net.skidcode.gh.server.network.protocol.MovePlayerPacket;
import net.skidcode.gh.server.network.protocol.PlaceBlockPacket;
import net.skidcode.gh.server.network.protocol.PlayerEquipmentPacket;
import net.skidcode.gh.server.network.protocol.RemoveBlockPacket;
import net.skidcode.gh.server.network.protocol.RequestChunkPacket;
import net.skidcode.gh.server.network.protocol.StartGamePacket;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.world.chunk.Chunk;

public class Player extends Entity implements CommandIssuer{
	
	public long clientID;
	public int port;
	public byte itemID;
	public String ip, identifier, nickname;
	
	public Player(String identifier, long clientID, String ip, int port) {
		super();
		this.clientID = clientID;
		this.port = port;
		this.ip = ip;
		this.identifier = identifier;
		this.posX = this.world.spawnX;
		this.posY = this.world.spawnY;
		this.posZ = this.world.spawnZ;
		
	}
	
	public void dataPacket(MinecraftDataPacket pk) {
		Server.handler.sendPacket(this, pk);
	}
	
	public void handlePacket(MinecraftDataPacket dp) {
		switch(dp.pid()) {
			case ProtocolInfo.LOGIN_PACKET: //TODO MessagePacket handle(but how to check..?)
				LoginPacket loginpacket = (LoginPacket)dp;
				this.nickname = loginpacket.nickname;
				this.world.addPlayer(this);
				StartGamePacket pk = new StartGamePacket();
				pk.seed = this.world.worldSeed;
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
			case ProtocolInfo.REMOVE_BLOCK_PACKET:
				RemoveBlockPacket rbp = (RemoveBlockPacket) dp;
				this.world.removeBlock(rbp.posX, rbp.posY, rbp.posZ);
				this.world.broadcastPacketFromPlayer(rbp, this);
				break;
			case ProtocolInfo.PLACE_BLOCK_PACKET:
				PlaceBlockPacket pbp = (PlaceBlockPacket) dp;
				this.world.placeBlock(pbp.posX, pbp.posY, pbp.posZ, pbp.id);
				this.world.broadcastPacketFromPlayer(pbp, this);
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
			case ProtocolInfo.PLAYER_EQUIPMENT_PACKET:
				PlayerEquipmentPacket pep = (PlayerEquipmentPacket) dp;
				if(pep.eid == this.eid) {
					this.itemID = pep.itemID;
					this.world.broadcastPacketFromPlayer(pep, this);
				}
				break;
			case ProtocolInfo.REQUEST_CHUNK_PACKET:
				RequestChunkPacket rcp = (RequestChunkPacket) dp;
				ChunkDataPacket cdp = new ChunkDataPacket();
				cdp.chunkX = rcp.chunkX;
				cdp.chunkZ = rcp.chunkZ;
				byte[] cd = new byte[16*16*128+16*16*64+16*16];
				int l = 0;
				Chunk c = this.world.chunks[rcp.chunkX][rcp.chunkZ];
				for (int z = 0; z < 16; ++z) { //TODO figure out what did i make
					for (int x = 0; x < 16; ++x) {
						cd[l++] = (byte) 0xff;//(byte) ((1 << 6));
						for(int y = 0; y < 8; ++y) {
							System.arraycopy(c.blockData[x][z], y << 4, cd, l, 16);
							l += 16;
							for(int bY = 0; bY < 8; ++bY) {
								cd[l++] = (byte) (c.blockMetadata[x][z][(y << 4) + (bY * 2)] + (c.blockMetadata[x][z][(y << 4) + (bY * 2) + 1] << 4));
							}
						}
					}
				}
				cdp.data = cd; 
				this.dataPacket(cdp);
				break;
			default:
				Logger.warn("Unknown PID: "+dp.pid());
				break;
		}
	}

	@Override
	public String getIssuerName() {
		return this.nickname;
	}

	@Override
	public void sendOutput(String s) {
		// TODO Auto-generated method stub
	}
}
