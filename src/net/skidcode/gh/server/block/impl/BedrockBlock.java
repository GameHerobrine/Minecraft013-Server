package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class BedrockBlock extends SolidBlock{

	public BedrockBlock(int id) {
		super(id, Material.stone);
		this.name = "Bedrock";
	}

}
