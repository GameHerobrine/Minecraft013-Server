package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.base.SolidBlock;
import net.skidcode.gh.server.block.material.Material;

public class InvisibleBedrockBlock extends SolidBlock{

	public InvisibleBedrockBlock(int id) {
		super(id, Material.stone);
		this.name = "Invisible Bedrock";
	}

}
