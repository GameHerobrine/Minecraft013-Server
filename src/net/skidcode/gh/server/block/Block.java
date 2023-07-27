package net.skidcode.gh.server.block;

import java.util.Random;

import net.skidcode.gh.server.block.impl.*;
import net.skidcode.gh.server.block.material.Material;
import net.skidcode.gh.server.network.protocol.RemoveBlockPacket;
import net.skidcode.gh.server.network.protocol.UpdateBlockPacket;
import net.skidcode.gh.server.player.Player;
import net.skidcode.gh.server.utils.Logger;
import net.skidcode.gh.server.utils.random.BedrockRandom;
import net.skidcode.gh.server.world.World;
//TODO block properties
public abstract class Block {
	
	public static Block[] blocks = new Block[256];
	public static boolean[] shouldTick = new boolean[256];
	public static int[] lightOpacity = new int[256];
	
	
	public static StoneBlock stone = new StoneBlock(1); //love old mcje code btw <<<
	public static GrassBlock grass = new GrassBlock(2);
	public static DirtBlock dirt = new DirtBlock(3);
	public static CobblestoneBlock cobblestone = new CobblestoneBlock(4);
	public static WoodBlock wood = new WoodBlock(5);
	public static BedrockBlock bedrock = new BedrockBlock(7);
	public static WaterBlock waterFlowing = new WaterBlock(8);
	public static WaterStillBlock waterStill = new WaterStillBlock(9);
	public static LavaBlock lavaFlowing = new LavaBlock(10);
	public static LavaStillBlock lavaStill = new LavaStillBlock(11);
	public static SandBlock sand = new SandBlock(12);
	public static GravelBlock gravel = new GravelBlock(13);
	public static GoldOreBlock oreGold = new GoldOreBlock(14);
	public static IronOreBlock oreIron = new IronOreBlock(15);
	public static CoalOreBlock oreCoal = new CoalOreBlock(16);
	public static LogBlock log = new LogBlock(17);
	public static LeavesBlock leaves = new LeavesBlock(18);
	public static GlassBlock glass = new GlassBlock(20);
	public static LapisOreBlock lapisOre = new LapisOreBlock(21);
	public static SandstoneBlock sandStone = new SandstoneBlock(24);
	public static YellowFlowerBlock yellowFlowerBlock = new YellowFlowerBlock(37);
	public static RoseBlock rose = new RoseBlock(38);
	public static BrownMushroomBlock brownMushroom = new BrownMushroomBlock(39);
	public static RedMushroomBlock redMushroom = new RedMushroomBlock(40);
	public static GoldBlock goldBlock = new GoldBlock(41);
	public static IronBlock ironBlock = new IronBlock(42);
	public static FullStoneSlabBlock fullStoneSlab = new FullStoneSlabBlock(43);
	public static StoneSlabBlock stoneSlab = new StoneSlabBlock(44);
	public static BrickBlock brick = new BrickBlock(45);
	public static TNTBlock tnt = new TNTBlock(46);
	public static ObsidianBlock obsidian = new ObsidianBlock(49);
	public static TorchBlock torch = new TorchBlock(50);
	public static WoodStairsBlock woodStairs = new WoodStairsBlock(53);
	public static DiamondOreBlock diamondOre = new DiamondOreBlock(56);
	public static DiamondBlock diamondBlock = new DiamondBlock(57);
	public static FarmlandBlock farmland = new FarmlandBlock(60);
	public static WoodenDoorBlock woodenDoor = new WoodenDoorBlock(64);
	public static LadderBlock ladder = new LadderBlock(65);
	public static StoneStairsBlock stoneStairs = new StoneStairsBlock(67);
	public static IronDoorBlock ironDoor = new IronDoorBlock(71);
	public static RedstoneOreBlock redstoneOre = new RedstoneOreBlock(73);
	public static GlowingRedstoneOreBlock glowingRedstoneOre = new GlowingRedstoneOreBlock(74);
	public static SnowLayerBlock snowLayer = new SnowLayerBlock(78);
	public static IceBlock ice = new IceBlock(79);
	public static CactusBlock cactus = new CactusBlock(81);
	public static ClayBlock clay = new ClayBlock(82);
	public static ReedsBlock reeds = new ReedsBlock(83);
	public static InvisibleBedrockBlock invisibleBedrock = new InvisibleBedrockBlock(95); //TODO destructible/indestructible
	public static WoolBlock wool = new WoolBlock(35, -1);
	public static WoolBlock wool_f = new WoolBlock(101, 0xf); //using ids instead of meta =/
	public static WoolBlock wool_e = new WoolBlock(102, 0xe);
	public static WoolBlock wool_d = new WoolBlock(103, 0xd);
	public static WoolBlock wool_c = new WoolBlock(104, 0xc);
	public static WoolBlock wool_b = new WoolBlock(105, 0xb);
	public static WoolBlock wool_a = new WoolBlock(106, 0xa);
	public static WoolBlock wool_9 = new WoolBlock(107, 0x9);
	public static WoolBlock wool_8 = new WoolBlock(108, 0x8);
	public static WoolBlock wool_7 = new WoolBlock(109, 0x7);
	public static WoolBlock wool_6 = new WoolBlock(110, 0x6);
	public static WoolBlock wool_5 = new WoolBlock(111, 0x5);
	public static WoolBlock wool_4 = new WoolBlock(112, 0x4);
	public static WoolBlock wool_3 = new WoolBlock(113, 0x3);
	public static WoolBlock wool_2 = new WoolBlock(114, 0x2);
	public static WoolBlock wool_1 = new WoolBlock(115, 0x1);
	public static InfoUpdateBlock updateGame = new InfoUpdateBlock(248);
	public static InfoUpdate2Block updateGame2 = new InfoUpdate2Block(249);
	public static FireBlock fire = new FireBlock(51);
	
	public void onNeighborBlockChanged(World world, int x, int y, int z, int meta) {}
	public void onBlockRemoved(World world, int x, int y, int z) {
		world.removeBlock(x, y, z);
		UpdateBlockPacket pk = new UpdateBlockPacket();
		pk.posX = x;
		pk.posY = (byte) y;
		pk.posZ = z;
		pk.id = 0;
		pk.metadata = 0;
		world.broadcastPacket(pk);
	}
	public void onBlockRemovedByPlayer(World world, int x, int y, int z, Player player) {
		world.removeBlock(x, y, z);
	}
	
	public void tick(World world, int x, int y, int z, BedrockRandom random) {
		
	}
	
	public void onBlockAdded(World world, int x, int y, int z) {
		
	}
	
	public boolean canSurvive(World world, int x, int y, int z) {
		return true;
	}
	
	public void onBlockPlacedByPlayer(World world, int x, int y, int z, int face, Player player) {
		world.placeBlockAndNotifyNearby(x, y, z, (byte) this.blockID);
	}
	
	public static void init() {}
	
	public Block(int id, Material m) {
		this.blockID = id;
		this.material = m;
		if(Block.blocks[id] instanceof Block) Logger.critical("ID "+id+" is occupied already!");
		else Block.blocks[id] = this;
	}
	
	public int blockID;
	public String name = "";
	public Material material;
	public boolean isSolid = true; //isRenderSolid method in 0.1.3
	public boolean isOpaque = true;
}
