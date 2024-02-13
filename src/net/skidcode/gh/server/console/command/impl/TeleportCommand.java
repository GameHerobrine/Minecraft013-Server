package net.skidcode.gh.server.console.command.impl;

import net.skidcode.gh.server.Server;
import net.skidcode.gh.server.console.command.CommandBase;
import net.skidcode.gh.server.console.command.CommandIssuer;
import net.skidcode.gh.server.console.command.ConsoleIssuer;
import net.skidcode.gh.server.network.protocol.MovePlayerPacket;
import net.skidcode.gh.server.player.Player;

public class TeleportCommand extends CommandBase{

	public TeleportCommand(String name, String desc) {
		super(name, desc);
	}

	@Override
	public String processCommand(CommandIssuer issuer, String... parameters) { //bad code =<
		
		int plength = parameters.length;
		if(plength == 4) {
			Player p = Server.getPlayerByNickname(parameters[3]);
			if(p == null) return "Player is not found";
			int x, y, z;
			try {
				x = Integer.parseInt(parameters[0]);
				y = Integer.parseInt(parameters[1]);
				z = Integer.parseInt(parameters[2]);
			}catch(Exception e) {
				return "Invalid coordinates.";
			}
			MovePlayerPacket pk = new MovePlayerPacket();
			pk.eid = p.eid;
			pk.posX = x;
			pk.posY = y;
			pk.posZ = z;
			pk.yaw = p.yaw;
			pk.pitch = p.pitch;
			p.dataPacket(pk);
		}else if(plength == 3) {
			if(issuer instanceof ConsoleIssuer) {
				return "You cannot teleport non-player objects.";
			}
			int x, y, z;
			try {
				x = Integer.parseInt(parameters[0]);
				y = Integer.parseInt(parameters[1]);
				z = Integer.parseInt(parameters[2]);
			}catch(Exception e) {
				return "Invalid coordinates.";
			}
			Player p = (Player) issuer;
			MovePlayerPacket pk = new MovePlayerPacket();
			pk.eid = p.eid;
			pk.posX = x;
			pk.posY = y;
			pk.posZ = z;
			pk.yaw = p.yaw;
			pk.pitch = p.pitch;
			p.dataPacket(pk);
			
			return "Teleported to ";
		}else {
			return "Usage: /tp <x> <y> <z> [player]";
		}
		
		return "";
	}

}
