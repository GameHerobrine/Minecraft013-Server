package net.skidcode.gh.server.world.biome.impl;

import net.skidcode.gh.server.world.biome.Biome;

public class TaigaBiome extends Biome{
	/** TODO
	 * _DWORD *__fastcall TaigaBiome::getTreeFeature(TaigaBiome *this, Random *a2)
		{
		  unsigned int v2; // r0
		  _DWORD *result; // r0
		
		  v2 = Random::genrand_int32(a2);
		  if ( v2 == 3 * (v2 / 3) )
		  {
		    result = operator new(4u);
		    *result = &off_10E3A8; off_10E3A8      DCD _ZN11PineFeatureD1Ev+1 ; PineFeature::~PineFeature()
		  }
		  else
		  {
		    result = operator new(4u);
		    *result = &off_10E3C0; off_10E3C0      DCD _ZN13SpruceFeatureD1Ev+1 ; SpruceFeature::~SpruceFeature()
		  }
		  return result;
		}
	 */
}
