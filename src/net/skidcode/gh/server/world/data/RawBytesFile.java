package net.skidcode.gh.server.world.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.skidcode.gh.server.utils.BinaryStream;
import net.skidcode.gh.server.world.World;

public abstract class RawBytesFile extends BinaryStream{
	public boolean created = true;
	public String filename;
	public RawBytesFile(byte[] buffer, int offset) {
		super(buffer, offset);
	}
	public RawBytesFile(byte[] buffer) {
		super(buffer, 0);
	}
	public RawBytesFile(String filename, int offset) throws IOException {
		super();
		this.filename = filename;
		Path p = Paths.get(filename);
		if(Files.exists(p)) {
			this.buffer = Files.readAllBytes(Paths.get(filename));
		}else {
			this.created = false;
		}
	}
	
	public abstract void parse() throws IOException;
	public abstract void save() throws IOException;
}
