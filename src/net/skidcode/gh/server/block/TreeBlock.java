package net.skidcode.gh.server.block;

import net.skidcode.gh.server.block.material.Material;

public class TreeBlock extends Block{

	public TreeBlock(int id) {
		super(id, Material.wood);
		this.unkField_4 = 20;
	}
	//TODO more methods
	/*
	int __fastcall TreeTile::onRemove(TreeTile *this, Level *a2, int a3, int a4, int a5)
	{
	  int result; // r0
	  int k; // [sp+2Ch] [bp-2Ch]
	  int j; // [sp+30h] [bp-28h]
	  int i; // [sp+34h] [bp-24h]

	  result = Level::hasChunksAt(a2, a3 - 5, a4 - 5, a5 - 5, a3 + 5, a4 + 5, a5 + 5);
	  if ( result )
	  {
	    for ( i = 0xFFFFFFFC; i <= 4; ++i )
	    {
	      for ( j = 0xFFFFFFFC; j <= 4; ++j )
	      {
	        for ( k = 0xFFFFFFFC; k <= 4; ++k )
	        {
	          result = (*(int (__fastcall **)(Level *, int, int, int))(*(_DWORD *)a2 + 8))(a2, a3 + i, a4 + j, k + a5);
	          if ( *(_DWORD *)(Tile::leaves + 8) == result )
	          {
	            result = (*(int (__fastcall **)(Level *, int, int, int))(*(_DWORD *)a2 + 0x10))(a2, a3 + i, a4 + j, k + a5);
	            if ( (result & 4) == 0 )
	              result = Level::setDataNoUpdate(a2, a3 + i, a4 + j, k + a5, result | 4);
	          }
	        }
	      }
	    }
	  }
	  return result;
	}
	*/
	
}
