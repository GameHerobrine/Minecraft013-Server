package net.skidcode.gh.server.world.nbt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.skidcode.gh.server.utils.BinaryStream;
import net.skidcode.gh.server.world.World;

public abstract class NBTFile extends BinaryStream{

	public NBTFile(byte[] buffer, int offset) {
		super(buffer, offset);
	}
	public NBTFile(byte[] buffer) {
		super(buffer, 0);
	}
	public NBTFile(String filename, int offset) throws IOException {
		super(Files.readAllBytes(Paths.get(filename)),  offset);
	}
	
	public abstract void parse(World world);
	public abstract void save(World world);
}
