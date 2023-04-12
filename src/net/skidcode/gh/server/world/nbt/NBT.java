package net.skidcode.gh.server.world.nbt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.skidcode.gh.server.utils.Binary;
import net.skidcode.gh.server.utils.BinaryStream;
import net.skidcode.gh.server.utils.Logger;

public class NBT {
	
	public static void main(String args[]) {
		System.out.println(System.getProperty("user.dir"));
		
		Path f = Paths.get("world/level.dat");
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(f);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		BinaryStream bs = new BinaryStream(bytes, 8);
		/**
RakNet::BitStream::Write<long>(a2, (unsigned __int8 *)this); seed?
RakNet::BitStream::Write<int>(a2, (char *)this + 4);
RakNet::BitStream::Write<int>(a2, (char *)this + 8);
RakNet::BitStream::Write<int>(a2, (char *)this + 0xC);
RakNet::BitStream::Write<long>(a2, (unsigned __int8 *)this + 0x10);
RakNet::BitStream::Write<long>(a2, (unsigned __int8 *)this + 0x18);
EpochTimeS = getEpochTimeS();
RakNet::BitStream::Write<int>(a2, &EpochTimeS); unix timestamp
RakNet::RakString::RakString((RakNet::RakString *)&v4, *((const char **)this + 0x23)); worldname
RakNet::RakString::Serialize((RakNet::RakString *)&v4, a2);
RakNet::RakString::~RakString((RakNet::RakString *)&v4);
		 */
		Logger.info(bs.getInt(), bs.getInt(), bs.getInt(), bs.getInt(), bs.getInt(), bs.getInt(), bs.getInt(), bs.getString());
	}
	
}
