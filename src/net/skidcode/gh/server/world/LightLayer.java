package net.skidcode.gh.server.world;

public enum LightLayer {
	SKY(15),
	BLOCK(0);
	
	public int defaultValue;
	
	LightLayer(int i) {
		this.defaultValue = i;
	}
	
}
