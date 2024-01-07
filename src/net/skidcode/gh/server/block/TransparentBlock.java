package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class TransparentBlock extends Block{

	public TransparentBlock(int id, Material m, boolean f6c) {
		super(id, m);
		this.unkField_6c = f6c;
		
		
	}
	/*int __fastcall TransparentTile::isSolidRender(TransparentTile *this)
	{
	  return 0;
	}*/ //TODO solidRender method
}
