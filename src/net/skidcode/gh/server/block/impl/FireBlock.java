package net.skidcode.gh.server.block.impl;

import net.skidcode.gh.server.block.Block;
import net.skidcode.gh.server.block.material.Material;

public class FireBlock extends Block{

	public FireBlock(int id) {
		super(id, Material.fire);
		this.isSolid = false;
		/**TODO blocks which burn
		 * Tile::Tile(v349, 0x33, 0x1F, (const Material *)Material::fire);
  *(_DWORD *)v349 = &off_C7395A48;
  v350 = (char *)v349 + 4 * *(_DWORD *)(Tile::wood + 8);
  *((_DWORD *)v350 + 0x1B) = 5;
  *((_DWORD *)v350 + 0x11B) = 0x14;
  v351 = (char *)v349 + 4 * *(_DWORD *)(Tile::treeTrunk + 8);
  *((_DWORD *)v351 + 0x1B) = 5;
  *((_DWORD *)v351 + 0x11B) = 5;
  v352 = (char *)v349 + 4 * *(_DWORD *)(Tile::leaves + 8);
  *((_DWORD *)v352 + 0x1B) = 0x1E;
  *((_DWORD *)v352 + 0x11B) = 0x3C;
  v353 = (char *)v349 + 4 * *(_DWORD *)(Tile::tnt + 8);
  *((_DWORD *)v353 + 0x1B) = 0xF;
  *((_DWORD *)v353 + 0x11B) = 0x64;
  v354 = (char *)v349 + 4 * *(_DWORD *)(Tile::cloth + 8);
  *((_DWORD *)v354 + 0x11B) = 0x3C;
  *((_DWORD *)v354 + 0x1B) = 0x1E;
		 */
		Block.shouldTick[id] = true;
	}

}
