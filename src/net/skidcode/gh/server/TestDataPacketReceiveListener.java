package net.skidcode.gh.server;

import net.skidcode.gh.server.event.EventListener;
import net.skidcode.gh.server.event.packet.DataPacketReceive;
import net.skidcode.gh.server.utils.Logger;

public class TestDataPacketReceiveListener extends EventListener<DataPacketReceive>{

	public TestDataPacketReceiveListener(DataPacketReceive event) {
		super(event);
	}

	@Override
	public void handleEvent(DataPacketReceive event) {
		Logger.info("Packet Received: "+ event.packet.pid()+", "+event.player.nickname);
	}

}
