package net.skidcode.gh.server.player;

import java.io.IOException;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.console.command.CommandIssuer;
import net.skidcode.gh.server.entity.Entity;
import net.skidcode.gh.server.event.EventRegistry;
import net.skidcode.gh.server.event.packet.DataPacketReceive;
import net.skidcode.gh.server.item.ItemInstance;
import net.skidcode.gh.server.network.MinecraftDataPacket;
import net.skidcode.gh.server.network.ProtocolInfo;
import net.skidcode.gh.server.network.protocol.AddPlayerPacket;
import net.skidcode.gh.server.network.protocol.ChunkDataPacket;
import net.skidcode.gh.server.network.protocol.LoginPacket;
import net.skidcode.gh.server.network.protocol.MessagePacket;
import net.skidcode.gh.server.network.protocol.MovePlayerPacket;
import net.skidcode.gh.server.network.protocol.PlaceBlockPacket;
import net.skidcode.gh.server.network.protocol.PlayerEquipmentPacket;
import net.skidcode.gh.server.network.protocol.RemoveBlockPacket;
import net.skidcode.gh.server.network.protocol.RequestChunkPacket;
import net.skidcode.gh.server.network.protocol.StartGamePacket;
import net.skidcode.gh.server.network.protocol.UpdateBlockPacket;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.world.chunk.Chunk;
import net.skidcode.gh.server.world.format.PlayerData;

public class Player extends Entity implements CommandIssuer{
	public long clientID;
	public int port;
	public int mtuSize;
	public byte itemID;
	public String ip, identifier, nickname = "";
	public boolean firstChunkData = true;
	public PlayerData playerdata;
	public boolean chunkDataSend[] = new boolean[256];
	public GameMode gamemode;
	
	public Player(String identifier, long clientID, String ip, int port) {
		super();
		this.setSize(0.6f, 1.8f);
		this.clientID = clientID;
		this.port = port;
		this.ip = ip;
		this.identifier = identifier;
		this.gamemode = new SurvivalMode(this);
	}
	
	public void sendMessage(String message) {
		MessagePacket pk = new MessagePacket();
		pk.message = message;
		this.dataPacket(pk);
	}
	
	public void dataPacket(MinecraftDataPacket pk) {
		Server.handler.sendPacket(this, pk);
	}
	
	public void onPlayerExit() {
		try {
			this.playerdata.save();
		} catch (Exception e) {
			Logger.error("Failed to save playerdata!");
			e.printStackTrace();
		}
		
		Server.broadcastMessage(this.nickname+" left the game.");
	}
	
	public void handlePacket(MinecraftDataPacket dp) {
		
		EventRegistry.handleEvent(new DataPacketReceive(this, dp));
		switch(dp.pid()) {
			case ProtocolInfo.MESSAGE_PACKET:
				Server.broadcastMessage(this.nickname+" : "+(((MessagePacket)dp).message));
				break;
			case ProtocolInfo.LOGIN_PACKET:
				LoginPacket loginpacket = (LoginPacket)dp;
				if(Server.getPlayerByNickname(loginpacket.nickname) != null) {
					Logger.info(String.format("%s was prevented to join because player with nickname %s is already in game.", this.identifier, loginpacket.nickname));
					break;
				}
				this.nickname = loginpacket.nickname;
				
				try {
					this.playerdata = new PlayerData(this);
				} catch (IOException e) {
					e.printStackTrace();
					Logger.error("Failed to create "+this.nickname+"'s playerdata!");
				}
				try {
					this.playerdata.parse();
				} catch (IOException e) {
					e.printStackTrace();
					Logger.error("Failed to parse playerdata!");
				}
				Server.world.addEntity(this);
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
					}
				}
				Logger.info("Player "+this.nickname+" joined the game. Position: "+this.posX+", "+this.posY+", "+this.posZ);
				break;
			case ProtocolInfo.REMOVE_BLOCK_PACKET:
				RemoveBlockPacket rbp = (RemoveBlockPacket) dp;
				
				if(!this.gamemode.destroyBlock(rbp.posX, rbp.posY, rbp.posZ)) {
					UpdateBlockPacket rollback = new UpdateBlockPacket();
					rollback.posX = rbp.posX;
					rollback.posY = rbp.posY;
					rollback.posZ = rbp.posZ;
					rollback.id = (byte) this.world.getBlockIDAt(rbp.posX, rbp.posY, rbp.posZ);
					rollback.metadata = (byte) this.world.getBlockMetaAt(rbp.posX, rbp.posY, rbp.posZ);
					this.dataPacket(rollback);
				}
				break;
			case ProtocolInfo.PLACE_BLOCK_PACKET:
				PlaceBlockPacket pbp = (PlaceBlockPacket) dp;
				
				switch(pbp.face) {
					case 0:
						++pbp.posY;
						break;
					case 1:
						--pbp.posY;
						break;
					case 2:
						++pbp.posZ;
						break;
					case 3:
						--pbp.posZ;
						break;
					case 4:
						++pbp.posX;
						break;
					case 5:
						--pbp.posX;
						break;
				}
				
				ItemInstance inst = new ItemInstance(pbp.id, 63, 0); //TODO better way to place it, this one is dangerous
				this.gamemode.useItemOn(inst, pbp.posX, pbp.posY, pbp.posZ, pbp.face);
				break;
			case ProtocolInfo.MOVE_PLAYER_PACKET_PACKET:
				MovePlayerPacket moveplayerpacket = (MovePlayerPacket)dp;
				this.setPosition(moveplayerpacket.posX, moveplayerpacket.posY, moveplayerpacket.posZ, moveplayerpacket.yaw, moveplayerpacket.pitch);
				moveplayerpacket.eid = this.eid;
				moveplayerpacket.setBuffer(new byte[] {});
				this.world.broadcastPacketFromPlayer(moveplayerpacket, this);
				
				break;
			case ProtocolInfo.PLAYER_EQUIPMENT_PACKET:
				PlayerEquipmentPacket pep = (PlayerEquipmentPacket) dp;
				if(pep.eid == this.eid) {
					int itemid = pep.itemID & 0xff;
					Block block = Block.blocks[itemid];
					if(block == null) {
						Logger.warn(String.format("%s tried to equip a block that is null! (ID: %d)", this.nickname, itemid));
						break;
					}
					
					this.itemID = pep.itemID;
					
					PlayerEquipmentPacket pepe = new PlayerEquipmentPacket(); //TODO fix
					pepe.eid = this.eid;
					pepe.itemID = this.itemID;
					this.world.broadcastPacketFromPlayer(pepe, this);
				}
				break;
			case ProtocolInfo.REQUEST_CHUNK_PACKET:
				if(this.firstChunkData) {
					this.onSpawned();
					this.firstChunkData = false;
				}
				RequestChunkPacket rcp = (RequestChunkPacket) dp;
				if(rcp.chunkX > 15 || rcp.chunkX < 0 || rcp.chunkZ > 15 || rcp.chunkZ < 0) {
					Logger.warn("Player "+this.nickname+" tried to get invalid chunk! ("+rcp.chunkX+" ,"+rcp.chunkZ+")");
					break;
				}
				ChunkDataPacket cdp = new ChunkDataPacket();
				cdp.chunkX = rcp.chunkX;
				cdp.chunkZ = rcp.chunkZ;
				byte[] cd = new byte[16*16*128+16*16*64+16*16];
				int l = 0;
				Chunk c = this.world.getChunk(rcp.chunkX, rcp.chunkZ);
				for (int z = 0; z < 16; ++z) {
					for (int x = 0; x < 16; ++x) {
						byte update = Server.sendFullChunks ? (byte) 0xff : c.updateMap[x][z];
						cd[l++] = update;
						
						for(int y = 0; y < 8; ++y) {
							if ((((update >> y) & 1) == 1))
							{
								int index = x << 11 | z << 7 | y << 4;
								System.arraycopy(c.blockData, index, cd, l, 16);
								l += 16;
								System.arraycopy(c.blockMetadata, index >> 1, cd, l, 8);
								l += 8;
							}
						}
						
					}
				}
				
				cdp.data = cd; 
				this.dataPacket(cdp);
				this.chunkDataSend[(rcp.chunkX << 4) | rcp.chunkZ] = true;
				break;
			default:
				Logger.warn("Unknown PID: "+dp.pid());
				break;
		}
	}
	
	public float getDestroySpeed() {
		return 0.5f;
	}
	
	public boolean canDestroy(Block block) {
		return true;
	}
	
	public void onSpawned() {
		for(Player p : Server.getPlayers()) {
			if(p.eid != this.eid) {
				PlayerEquipmentPacket pep = new PlayerEquipmentPacket();
				pep.eid = p.eid;
				pep.itemID = p.itemID;
				this.dataPacket(pep);
				
				AddPlayerPacket pkk2 = new AddPlayerPacket();
				pkk2.clientID = this.clientID;
				pkk2.eid = this.eid;
				pkk2.nickname = this.nickname;
				pkk2.posX = this.posX;
				pkk2.posY = this.posY;
				pkk2.posZ = this.posZ;
				p.dataPacket(pkk2);
			}
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
