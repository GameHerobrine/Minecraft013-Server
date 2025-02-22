package net.skidcode.gh.server.block;

import java.util.Random;

import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.item.BlockItem;
import net.skidcode.gh.server.item.Item;
import net.skidcode.gh.server.network.protocol.RemoveBlockPacket;
import net.skidcode.gh.server.network.protocol.UpdateBlockPacket;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.AABB;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;

//TODO block properties
public class Block {
	public static Block[] blocks = new Block[256];
	public static boolean[] shouldTick = new boolean[256];
	public static boolean[] solid = new boolean[256];
	public static int[] lightBlock = new int[256];
	public static int[] lightEmission = new int[256];
	public String description;
	public float destroyTime = 0;
	public float explosionResistance = 0;
	public float slipperiness = 1.0f;
	public int unkField_4 = 0;
	public int unkField_70;
	public boolean unkField_6c;
	public boolean isFullTile = true;
	public AABB boundingBox;
	
	public float minX = 0, minY = 0, minZ = 0, maxX = 1, maxY = 1, maxZ = 1;
	
	public static Block stone = new StoneBlock(1).setExplodeable(10).setDestroyTime(1.5f).setDescription("stone");
	public static Block grass = new GrassBlock(2).setDestroyTime(0.6f).setDescription("grass");
	public static Block dirt = new DirtBlock(3).setDestroyTime(0.5f).setDescription("dirt");
	public static Block cobblestone = new Block(4, Material.stone).setDestroyTime(2.0f).setExplodeable(10.0f).setDescription("stonebrick");
	public static Block wood = new Block(5, Material.wood).setDestroyTime(2.0f).setExplodeable(5.0f).setDescription("wood");
	public static Block bedrock = new Block(7, Material.stone).setDestroyTime(-1.0f).setExplodeable(6000000.0f).setDescription("bedrock");
	public static Block waterFlowing = new LiquidBlockDynamic(8, Material.water).setDestroyTime(100.0f).setLightBlock(3).setDescription("water");
	public static Block waterStill = new LiquidBlockStatic(9, Material.water).setDestroyTime(100.0f).setLightBlock(3).setDescription("water");
	public static Block lavaFlowing = new LiquidBlockDynamic(10, Material.lava).setDestroyTime(0.0f).setLightEmmision(1.0f).setLightBlock(0xff).setDescription("lava");
	public static Block lavaStill = new LiquidBlockStatic(11, Material.lava).setDestroyTime(100.0f).setLightEmmision(1.0f).setLightBlock(0xff).setDescription("lava");
	public static Block sand = new SandBlock(12).setDestroyTime(0.5f).setDescription("sand");
	public static Block gravel = new GravelBlock(13).setDestroyTime(0.6f).setDescription("gravel");
	public static Block oreGold = new OreBlock(14).setDestroyTime(3.0f).setExplodeable(5.0f).setDescription("oreGold");
	public static Block oreIron = new OreBlock(15).setDestroyTime(3.0f).setExplodeable(5.0f).setDescription("oreIron");
	public static Block oreCoal = new OreBlock(16).setDestroyTime(3.0f).setExplodeable(5.0f).setDescription("oreCoal");
	public static Block log = new TreeBlock(17).setDestroyTime(2.0f).setDescription("log");
	public static Block leaves = new LeafBlock(18).setDestroyTime(0.2f).setLightBlock(1).setDescription("leaves");
	public static Block glass = new GlassBlock(20, Material.glass, false).setDestroyTime(0.3f).setDescription("glass");
	public static Block lapisOre = new OreBlock(21).setDestroyTime(3.0f).setExplodeable(5.0f).setDescription("oreLapis");
	public static Block sandStone = new SandstoneBlock(24).setDestroyTime(0.8f).setDescription("sandStone");
	public static Block flower = new BushBlock(37).setDestroyTime(0.0f).setDescription("flower");
	public static Block rose = new BushBlock(38).setDestroyTime(0.0f).setDescription("rose");
	public static Block mushroomBrown = new BushBlock(39).setDestroyTime(0.0f).setLightEmmision(0.125f).setDescription("mushroom");
	public static Block mushroomRed = new BushBlock(40).setDestroyTime(0.0f).setDescription("mushroom");
	public static Block goldBlock = new MetalBlock(41).setDestroyTime(3.0f).setExplodeable(10.0f).setDescription("blockGold");
	public static Block ironBlock = new MetalBlock(42).setDestroyTime(3.0f).setExplodeable(10.0f).setDescription("blockIron");
	public static Block fullStoneSlab = new StoneSlabBlock(43, true).setDestroyTime(2.0f).setExplodeable(10.0f).setDescription("stoneSlab");
	public static Block stoneSlab = new StoneSlabBlock(44, false).setDestroyTime(2.0f).setExplodeable(10.0f).setDescription("stoneSlab");
	public static Block brick = new Block(45, Material.stone).setDestroyTime(2.0f).setExplodeable(10.0f).setDescription("brick");
	public static Block tnt = new TntBlock(46).setDestroyTime(0.0f).setDescription("tnt");
	public static Block obsidian = new ObsidianBlock(49).setDestroyTime(10.0f).setExplodeable(2000f).setDescription("obsidian");
	public static Block torch = new TorchBlock(50).setDestroyTime(0.0f).setLightEmmision(0.9375f).setDescription("torch");
	public static Block woodStairs = new StairBlock(53, Block.wood).setDescription("stairsWood");
	public static Block diamondOre = new OreBlock(56).setDestroyTime(3.0f).setExplodeable(5.0f).setDescription("oreDiamond");
	public static Block diamondBlock = new MetalBlock(57).setDestroyTime(5.0f).setExplodeable(10.0f).setDescription("blockDiamond");
	public static Block farmland = new FarmBlock(60).setDestroyTime(0.6f).setDescription("farmland");
	public static Block woodenDoor = new DoorBlock(64, Material.wood).setDestroyTime(3.0f).setDescription("doorWood");
	public static Block ladder = new LadderBlock(65).setDestroyTime(0.4f).setDescription("ladder");
	public static Block stoneStairs = new StairBlock(67, Block.stone).setDescription("stairsStone");
	public static Block ironDoor = new DoorBlock(71, Material.metal).setDestroyTime(5.0f).setDescription("doorIron");
	public static Block redstoneOre = new RedstoneOreBlock(73, false).setDestroyTime(3.0f).setExplodeable(5.0f).setDescription("oreRedstone");
	public static Block glowingRedstoneOre = new RedstoneOreBlock(74, true).setDestroyTime(3.0f).setLightEmmision(0.625f).setExplodeable(5.0f).setDescription("oreRedstone");
	public static Block snowLayer = new TopSnowBlock(78).setDestroyTime(0.1f).setDescription("snow");
	public static Block ice = new IceBlock(79).setDestroyTime(0.5f).setLightBlock(3).setDescription("ice");
	public static Block cactus = new CactusBlock(81).setDestroyTime(0.4f).setDescription("cactus");
	public static Block clay = new ClayBlock(82).setDestroyTime(0.6f).setDescription("clay");
	public static Block reeds = new ReedBlock(83).setDestroyTime(0.0f).setDescription("reeds");
	public static Block invisibleBedrock = new InvisibleBlock(95, Material.stone).setDestroyTime(-1.0f).setExplodeable(6000000.0f);
	
	public static Block wool = new ClothBlock(35, -49).setDestroyTime(0.8f);
	public static Block wool_f = new ClothBlock(101, 0xf).setDestroyTime(0.8f); //using ids instead of meta =/
	public static Block wool_e = new ClothBlock(102, 0xe).setDestroyTime(0.8f);
	public static Block wool_d = new ClothBlock(103, 0xd).setDestroyTime(0.8f);
	public static Block wool_c = new ClothBlock(104, 0xc).setDestroyTime(0.8f);
	public static Block wool_b = new ClothBlock(105, 0xb).setDestroyTime(0.8f);
	public static Block wool_a = new ClothBlock(106, 0xa).setDestroyTime(0.8f);
	public static Block wool_9 = new ClothBlock(107, 0x9).setDestroyTime(0.8f);
	public static Block wool_8 = new ClothBlock(108, 0x8).setDestroyTime(0.8f);
	public static Block wool_7 = new ClothBlock(109, 0x7).setDestroyTime(0.8f);
	public static Block wool_6 = new ClothBlock(110, 0x6).setDestroyTime(0.8f);
	public static Block wool_5 = new ClothBlock(111, 0x5).setDestroyTime(0.8f);
	public static Block wool_4 = new ClothBlock(112, 0x4).setDestroyTime(0.8f);
	public static Block wool_3 = new ClothBlock(113, 0x3).setDestroyTime(0.8f);
	public static Block wool_2 = new ClothBlock(114, 0x2).setDestroyTime(0.8f);
	public static Block wool_1 = new ClothBlock(115, 0x1).setDestroyTime(0.8f);
	public static Block info_updateGame1 = new Block(248, Material.dirt).setDestroyTime(5.0f);
	public static Block updateGame2 = new Block(249, Material.dirt).setDestroyTime(0.8f);
	public static Block fire = new FireBlock(51).setDestroyTime(0.0f).setLightEmmision(1.0f);
	
	
	static {
		for(int i = 0; i < 256; ++i) {
			if(Block.blocks[i] != null) {
				if(Item.items[i] == null) {
					Item.items[i] = new BlockItem(i - 256);
				}
			}
		}
	}
	
	public void neighborChanged(World world, int x, int y, int z, int meta) {}
	public void onBlockRemoved(World world, int x, int y, int z) {
		world.placeBlock(x, y, z, (byte)0, (byte)0);
		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.posX = x;
		pk.posY = (byte) y;
		pk.posZ = z;
		pk.id = 0;
		pk.metadata = 0;
		world.broadcastPacket(pk);
	}
	
	public Block setExplodeable(float f) {
		this.explosionResistance = f * 3.0f;
		return this;
	}
	
	public void setShape(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	public Block setDestroyTime(float f) {
		this.destroyTime = f;
		return this;
	}
	
	public Block setDescription(String s) {
		this.description = s;
		return this;
	}
	
	public Block setTicking(boolean b) {
		Block.shouldTick[this.blockID] = b;
		return this;
	}
	
	public Block setLightBlock(int i) {
		Block.lightBlock[this.blockID] = i;
		return this;
	}
	
	public Block setLightEmmision(float f) {
		Block.lightEmission[this.blockID] = (int)(f * 15f);
		return this;
	}
	
	public void onBlockRemovedByPlayer(World world, int x, int y, int z, Player player) {
		world.placeBlock(x, y, z, (byte)0, (byte)0);
	}
	
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		
	}
	
	public void onBlockAdded(World world, int x, int y, int z) { //TODO rename to onPlace
		
	}
	
	public void destroy(World world, int x, int y, int z, int meta) {
		
	}
	
	public void onRemove(World world, int x, int y, int z) {
		/*
		 * TODO leaf, stairs, trunk
			LeafTile::onRemove(Level *,int,int,int)
			StairTile::onRemove(Level *,int,int,int)
			TreeTile::onRemove(Level *,int,int,int)
		 */
	}
	
	public boolean mayPlace(World world, int x, int y, int z) {
		int blockID = world.getBlockIDAt(x, y, z);
		
		if(blockID == 0) return true;
		
		return Block.blocks[blockID].material.isLiquid;
	}
	
	public boolean canSurvive(World world, int x, int y, int z) {
		return true;
	}
	
	public boolean isSolidRender() {
		return true;
	}
	
	public void init() {
		Block.solid[this.blockID] = this.isSolidRender();
		
		int lightBlock = this.isSolidRender() ? 255 : 0;
		Block.lightBlock[this.blockID] = lightBlock;
		//TODO Block.translucent
		
	}
	
	public Block(int id, Material m) {
		this.blockID = id;
		this.material = m;
		this.unkField_4 = 1;
		this.boundingBox = new AABB(0, 0, 0, 1, 1, 1);
		if(Block.blocks[id] instanceof Block) Logger.critical("ID "+id+" is occupied already!");
		else Block.blocks[id] = this;
		this.init();
	}
	
	public int getTickDelay() {
		return 10;
	}
	
	public float getDestroyProgress(Player player) {
		if(this.destroyTime < 0) return 0;
		
		if(!player.canDestroy(this)) { //never called
			return (1 - this.destroyTime) / 100;
		}
		
		return player.getDestroySpeed() / this.destroyTime / 30;
	}
	
	public int blockID;
	public String name = "";
	public Material material;
	public boolean isOpaque = true;

	public boolean use(World world, int x, int y, int z, Player player) {
		return false;
	}
	
	public void setPlacedBy(World world, int x, int y, int z, Player player) {
		
	}
	
	public void setPlacedOnFace(World world, int x, int y, int z, int side) {
		
	}
	
}
