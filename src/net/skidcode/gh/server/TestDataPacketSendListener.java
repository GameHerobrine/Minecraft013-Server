package net.skidcode.gh.server;

import net.skidcode.gh.server.event.EventListener;
import net.skidcode.gh.server.event.packet.DataPacketSend;
import net.skidcode.gh.server.utils.Logger;

public class TestDataPacketSendListener extends EventListener<DataPacketSend>{

	public TestDataPacketSendListener(DataPacketSend event) {
		super(event);
	}

	@Override
	public void handleEvent(DataPacketSend event) {
		Logger.info("Packet Sent: "+ event.packet.pid()+", "+event.player.nickname);
	}

}
