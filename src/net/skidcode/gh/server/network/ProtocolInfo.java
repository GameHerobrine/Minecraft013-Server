package net.skidcode.gh.server.network;
import net.skidcode.gh.server.network.protocol.AddPlayerPacket;
import net.skidcode.gh.server.network.protocol.ChunkDataPacket;
import net.skidcode.gh.server.network.protocol.LoginPacket;
import net.skidcode.gh.server.network.protocol.MessagePacket;
import net.skidcode.gh.server.network.protocol.MovePlayerPacket;
import net.skidcode.gh.server.network.protocol.PlaceBlockPacket;
import net.skidcode.gh.server.network.protocol.PlayerEquipmentPacket;
import net.skidcode.gh.server.network.protocol.RemoveBlockPacket;
import net.skidcode.gh.server.network.protocol.RemoveEntityPacket;
import net.skidcode.gh.server.network.protocol.RequestChunkPacket;
import net.skidcode.gh.server.network.protocol.StartGamePacket;
import net.skidcode.gh.server.network.protocol.UpdateBlockPacket;

/*
LoginPacket 0x86 (string)
MessagePacket 0x87 (string)
StartGamePacket 0x88 (long, int, int, float, float, float)
AddPlayerPacket 0x89 (guid, string, int, float, float, float)
RemoveEntityPacket 0x8a (int)
MovePlayerPacket 0x8b (int, float, float, float, float, float)
/ 0x8c (int, int, int, ubyte, ubyte, ubyte)
RemoveBlockPacket 0x8d (int, int, int, ubyte)
UpdateBlockPacket 0x8e (int, int, ubyte, ubyte, ubyte)
RequestChunkPacket 0x8f (int, int)
ChunkDataPacket 0x90 (int, int, write, ubyte, write, write, write, int, int, write, write, write)
PlayerEquipmentPacket 0x91 (int, ubyte)
*/
@SuppressWarnings("rawtypes")
public final class ProtocolInfo {
	public static final byte LOGIN_PACKET = (byte) 0x86;
	public static final byte MESSAGE_PACKET = (byte) 0x87;
	public static final byte START_GAME_PACKET = (byte) 0x88;
	public static final byte ADD_PLAYER_PACKET = (byte) 0x89;
	public static final byte REMOVE_ENTITY_PACKET = (byte) 0x8a;
	public static final byte MOVE_PLAYER_PACKET_PACKET = (byte) 0x8b;
	public static final byte PLACE_BLOCK_PACKET = (byte) 0x8c;
	public static final byte REMOVE_BLOCK_PACKET = (byte) 0x8d;
	public static final byte UPDATE_BLOCK_PACKET = (byte) 0x8e;
	public static final byte REQUEST_CHUNK_PACKET = (byte) 0x8f;
	public static final byte CHUNK_DATA_PACKET = (byte) 0x90;
	public static final byte PLAYER_EQUIPMENT_PACKET = (byte) 0x91;
	public static Class[] packets = new Class[256];
	
	static {
		packets[LOGIN_PACKET & 0xFF] = LoginPacket.class;
		packets[MESSAGE_PACKET & 0xFF] = MessagePacket.class;
		packets[START_GAME_PACKET & 0xFF] = StartGamePacket.class;
		packets[ADD_PLAYER_PACKET & 0xFF] = AddPlayerPacket.class;
		packets[REMOVE_ENTITY_PACKET & 0xFF] = RemoveEntityPacket.class;
		packets[MOVE_PLAYER_PACKET_PACKET & 0xFF] = MovePlayerPacket.class;
		packets[PLACE_BLOCK_PACKET & 0xFF] = PlaceBlockPacket.class;
		packets[REMOVE_BLOCK_PACKET & 0xFF] = RemoveBlockPacket.class;
		packets[UPDATE_BLOCK_PACKET & 0xFF] = UpdateBlockPacket.class;
		packets[REQUEST_CHUNK_PACKET & 0xFF] = RequestChunkPacket.class;
		packets[CHUNK_DATA_PACKET & 0xFF] = ChunkDataPacket.class;
		packets[PLAYER_EQUIPMENT_PACKET & 0xFF] = PlayerEquipmentPacket.class;
	}
	
	public ProtocolInfo() {throw new IllegalArgumentException("You are not allowed to create instances of ProtocolInfo.(do not use unsafe pwease)");}
}
