package net.skidcode.gh.server.world.biome.impl;

import net.skidcode.gh.server.world.biome.Biome;

public class ForestBiome extends Biome{
	/** TODO
	 * _DWORD *__fastcall ForestBiome::getTreeFeature(ForestBiome *this, Random *a2)
		{
		  unsigned int v3; // r0
		  _DWORD *result; // r0
		
		  v3 = Random::genrand_int32(a2);
		  if ( v3 == 5 * (v3 / 5) )
		  {
		    result = operator new(4u);
		    *result = &off_10E370;  off_10E370      DCD _ZN12BirchFeatureD1Ev+1 ; BirchFeature::~BirchFeature()
		  }
		  else
		  {
		    Random::genrand_int32(a2);
		    result = operator new(4u);
		    *result = &off_10E2E0; off_10E2E0      DCD _ZN11TreeFeatureD1Ev+1 ; TreeFeature::~TreeFeature()
		  }
		  return result;
		}
	 */
}
