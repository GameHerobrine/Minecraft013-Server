package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;

public class SnowLayerBlock extends Block{ //TODO some baseclass for non-solid?

	public SnowLayerBlock(int id) {
		super(id, Material.topSnow);
		this.isSolid = false;
		Block.shouldTick[id] = true;
	}

}
