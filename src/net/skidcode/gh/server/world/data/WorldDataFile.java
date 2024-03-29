package net.skidcode.gh.server.world.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.skidcode.gh.server.utils.BinaryStream;
import net.skidcode.gh.server.world.World;

public abstract class WorldDataFile extends BinaryStream{

	public WorldDataFile(byte[] buffer, int offset) {
		super(buffer, offset);
	}
	public WorldDataFile(byte[] buffer) {
		super(buffer, 0);
	}
	public WorldDataFile(String filename, int offset) throws IOException {
		super(Files.readAllBytes(Paths.get(filename)),  offset);
	}
	public WorldDataFile(Path path, int offset) throws IOException {
		super(Files.readAllBytes(path),  offset);
	}
	public abstract void parse(World world);
	public abstract void save(World world);
}
