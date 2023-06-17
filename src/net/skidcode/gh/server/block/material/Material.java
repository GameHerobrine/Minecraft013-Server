package net.skidcode.gh.server.block.material;
/*
  v8 = operator new(8u);
  *(_DWORD *)v8 = &off_10E680;
  *(_BYTE *)(v8 + 4) = 1;
  Material::leaves = v8;
  v11 = operator new(8u);
  *(_DWORD *)v11 = &off_10E680;
  *(_BYTE *)(v11 + 4) = 1;
  Material::cloth = v11;
    v16 = operator new(8u);
  *(_DWORD *)v16 = &off_10E680;
  *(_BYTE *)(v16 + 4) = 1;
  Material::explosive = v16;
  
 */
public class Material {
	public static Material air = new Material().setNonSolid().setNoBlockLight().setNoBlockMotion();
	public static Material dirt = new Material();
	public static Material wood = new Material().setFlameable();
	public static Material stone = new Material();
	public static Material metal = new Material();
	public static Material water = new Material().setLiquid().setNoBlockLight().setNoBlockMotion(); //while it is liquid, mcpe 0.1's Material::isLiquid always returns 0  
	public static Material lava = new Material().setLiquid().setNoBlockLight().setNoBlockMotion();
	public static Material leaves = new Material().setFlameable();
	public static Material plant = new Material().setNonSolid(); //TODO check
	public static Material sponge = new Material();
	public static Material cloth = new Material().setFlameable(); //wool
	public static Material fire = new Material();
	public static Material sand = new Material();
	public static Material decoration = new Material().setNoBlockLight().setNoBlockMotion().setNonSolid();
	public static Material glass = new Material();
	public static Material explosive = new Material().setFlameable();
	public static Material coral = new Material();
	public static Material ice = new Material();
	public static Material topSnow = new Material();
	public static Material snow = new Material();
	public static Material cactus = new Material();
	public static Material clay = new Material();
	public static Material vegetable = new Material();
	public static Material portal = new Material(); //funny that nothing will appear with this material until ~0.12
	public static Material cake = new Material();
	public Material setFlameable() {
		this.isFlameable = true;
		return this;
	}
	public Material setNonSolid() {
		this.isSolid = false;
		return this;
	}
	
	public Material setNoBlockLight() {
		this.blocksLight = false;
		return this;
	}
	public Material setNoBlockMotion() {
		this.blocksMotion = false;
		return this;
	}
	
	public Material setLiquid() {
		this.isLiquid = true;
		this.isSolid = false;
		return this;
	}
	
	public boolean isFlameable = false; //MCPE Offset: 4
	public boolean isSolid = true;
	public boolean isLiquid = false;
	public boolean blocksLight = true;
	public boolean blocksMotion = true;
}
