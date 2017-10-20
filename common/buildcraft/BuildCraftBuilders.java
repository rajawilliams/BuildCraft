/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDropper;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import buildcraft.api.blueprints.BlueprintDeployer;
import buildcraft.api.blueprints.BuilderAPI;
import buildcraft.api.blueprints.ISchematicRegistry;
import buildcraft.api.blueprints.SchematicBlock;
import buildcraft.api.blueprints.SchematicEntity;
import buildcraft.api.blueprints.SchematicFactory;
import buildcraft.api.blueprints.SchematicMask;
import buildcraft.api.core.BCLog;
import buildcraft.api.core.JavaTools;
import buildcraft.api.filler.FillerManager;
import buildcraft.api.filler.IFillerPattern;
import buildcraft.api.statements.StatementManager;
import buildcraft.builders.BlockArchitect;
import buildcraft.builders.BlockBlueprintLibrary;
import buildcraft.builders.BlockBuildTool;
import buildcraft.builders.BlockBuilder;
import buildcraft.builders.BlockConstructionMarker;
import buildcraft.builders.BlockFiller;
import buildcraft.builders.BlockMarker;
import buildcraft.builders.BlockPathMarker;
import buildcraft.builders.BuilderProxy;
import buildcraft.builders.EventHandlerBuilders;
import buildcraft.builders.GuiHandler;
import buildcraft.builders.HeuristicBlockDetection;
import buildcraft.builders.ItemBlueprintStandard;
import buildcraft.builders.ItemBlueprintTemplate;
import buildcraft.builders.ItemConstructionMarker;
import buildcraft.builders.TileArchitect;
import buildcraft.builders.TileBlueprintLibrary;
import buildcraft.builders.TileBuilder;
import buildcraft.builders.TileConstructionMarker;
import buildcraft.builders.TileFiller;
import buildcraft.builders.TileMarker;
import buildcraft.builders.TilePathMarker;
import buildcraft.builders.blueprints.BlueprintDatabase;
import buildcraft.builders.schematics.SchematicAir;
import buildcraft.builders.schematics.SchematicBed;
import buildcraft.builders.schematics.SchematicBlockCreative;
import buildcraft.builders.schematics.SchematicCactus;
import buildcraft.builders.schematics.SchematicCustomStack;
import buildcraft.builders.schematics.SchematicDirt;
import buildcraft.builders.schematics.SchematicDoor;
import buildcraft.builders.schematics.SchematicEnderChest;
import buildcraft.builders.schematics.SchematicFactoryBlock;
import buildcraft.builders.schematics.SchematicFactoryEntity;
import buildcraft.builders.schematics.SchematicFactoryMask;
import buildcraft.builders.schematics.SchematicFarmland;
import buildcraft.builders.schematics.SchematicFire;
import buildcraft.builders.schematics.SchematicGlassPane;
import buildcraft.builders.schematics.SchematicGravel;
import buildcraft.builders.schematics.SchematicHanging;
import buildcraft.builders.schematics.SchematicIgnore;
import buildcraft.builders.schematics.SchematicLever;
import buildcraft.builders.schematics.SchematicMinecart;
import buildcraft.builders.schematics.SchematicPiston;
import buildcraft.builders.schematics.SchematicPortal;
import buildcraft.builders.schematics.SchematicPumpkin;
import buildcraft.builders.schematics.SchematicRail;
import buildcraft.builders.schematics.SchematicRedstoneDiode;
import buildcraft.builders.schematics.SchematicRedstoneLamp;
import buildcraft.builders.schematics.SchematicRedstoneWire;
import buildcraft.builders.schematics.SchematicRotate;
import buildcraft.builders.schematics.SchematicSeeds;
import buildcraft.builders.schematics.SchematicSign;
import buildcraft.builders.schematics.SchematicSkull;
import buildcraft.builders.schematics.SchematicStairs;
import buildcraft.builders.schematics.SchematicStandalone;
import buildcraft.builders.schematics.SchematicStone;
import buildcraft.builders.schematics.SchematicTileCreative;
import buildcraft.builders.schematics.SchematicTripWireHook;
import buildcraft.builders.schematics.SchematicWallSide;
import buildcraft.builders.statements.ActionFiller;
import buildcraft.builders.statements.BuildersActionProvider;
import buildcraft.builders.urbanism.BlockUrbanist;
import buildcraft.builders.urbanism.TileUrbanist;
import buildcraft.core.BlockBuildCraft;
import buildcraft.core.DefaultProps;
import buildcraft.core.InterModComms;
import buildcraft.core.Version;
import buildcraft.core.blueprints.RealBlueprintDeployer;
import buildcraft.core.blueprints.SchematicRegistry;
import buildcraft.core.builders.patterns.FillerRegistry;
import buildcraft.core.builders.patterns.PatternBox;
import buildcraft.core.builders.patterns.PatternClear;
import buildcraft.core.builders.patterns.PatternCylinder;
import buildcraft.core.builders.patterns.PatternFill;
import buildcraft.core.builders.patterns.PatternFlatten;
import buildcraft.core.builders.patterns.PatternFrame;
import buildcraft.core.builders.patterns.PatternHorizon;
import buildcraft.core.builders.patterns.PatternPyramid;
import buildcraft.core.builders.patterns.PatternStairs;
import buildcraft.core.proxy.CoreProxy;

@Mod(name = "BuildCraft Builders", version = Version.VERSION, useMetadata = false, modid = "BuildCraftBuilders", dependencies = DefaultProps.DEPENDENCY_CORE)
public class BuildCraftBuilders extends BuildCraftMod {

	@Mod.Instance("BuildCraftBuilders")
	public static BuildCraftBuilders instance;

	public static final char BPT_SEP_CHARACTER = '-';
	public static final int LIBRARY_PAGE_SIZE = 12;
	public static final int MAX_BLUEPRINTS_NAME_SIZE = 32;
	public static BlockBuildTool buildToolBlock;
	public static BlockMarker markerBlock;
	public static BlockPathMarker pathMarkerBlock;
	public static BlockConstructionMarker constructionMarkerBlock;
	public static BlockFiller fillerBlock;
	public static BlockBuilder builderBlock;
	public static BlockArchitect architectBlock;
	public static BlockBlueprintLibrary libraryBlock;
	public static BlockUrbanist urbanistBlock;
	public static ItemBlueprintTemplate templateItem;
	public static ItemBlueprintStandard blueprintItem;

	public static ActionFiller[] fillerActions;

	public static BlueprintDatabase serverDB;
	public static BlueprintDatabase clientDB;

	public static boolean debugPrintSchematicList = false;
	
	@Mod.EventHandler
	public void loadConfiguration(FMLPreInitializationEvent evt) {
		String blueprintServerDir = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL,
				"blueprints.serverDir",
				"\"$MINECRAFT" + File.separator + "config" + File.separator + "buildcraft" + File.separator
						+ "blueprints" + File.separator + "server\"").getString();

		String blueprintLibraryOutput = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL,
				"blueprints.libraryOutput", "\"$MINECRAFT" + File.separator + "blueprints\"").getString();

		String [] blueprintLibraryInput = BuildCraftCore.mainConfiguration.get(Configuration.CATEGORY_GENERAL,
				"blueprints.libraryInput", new String []
				{
						// expected location
						"\"$MINECRAFT" + File.separator + "blueprints\"",
						// legacy beta BuildCraft
						"\"$MINECRAFT" + File.separator + "config" + File.separator + "buildcraft" + File.separator
								+ "blueprints" + File.separator + "client\"",
						// inferred user download location
						"\"" + getDownloadsDir() + "\""
				}
				).getStringList().clone();

		blueprintServerDir = JavaTools.stripSurroundingQuotes(replacePathVariables(blueprintServerDir));
		blueprintLibraryOutput = JavaTools.stripSurroundingQuotes(replacePathVariables(blueprintLibraryOutput));

		for (int i = 0; i < blueprintLibraryInput.length; ++i) {
			blueprintLibraryInput[i] = JavaTools.stripSurroundingQuotes(replacePathVariables(blueprintLibraryInput[i]));
		}
		
		Property printSchematicList = BuildCraftCore.mainConfiguration.get("debug", "blueprints.printSchematicList", false);
		debugPrintSchematicList = printSchematicList.getBoolean();

		if (BuildCraftCore.mainConfiguration.hasChanged()) {
			BuildCraftCore.mainConfiguration.save();
		}

		serverDB = new BlueprintDatabase();
		clientDB = new BlueprintDatabase();

		serverDB.init(new String[] {blueprintServerDir}, blueprintServerDir);
		clientDB.init(blueprintLibraryInput, blueprintLibraryOutput);
	}

	private static String getDownloadsDir() {
		final String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("nix") || os.contains("lin") || os.contains("mac")) {
			// Linux, Mac or other UNIX
			// According XDG specification every user-specified folder can be localized
			// or even moved to any destination, so we obtain real path with xdg-user-dir
			try {
				Process process = Runtime.getRuntime().exec(new String[] {"xdg-user-dir", "DOWNLOAD"});
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
				process.waitFor();
				String line = reader.readLine().trim();
				reader.close();

				if (line.length() > 0) {
					return line;
				}
			} catch (Exception ignored) {
				// Very bad, we have a error while obtaining xdg dir :(
				// Just ignore, uses default dir
			}
		}
		// Windows or unknown system
		return "$HOME" + File.separator + "Downloads";
	}

	private String replacePathVariables(String path) {
		String result = path.replace("$DOWNLOADS", getDownloadsDir());
		result = result.replace("$HOME", System.getProperty("user.home"));

		if (Launch.minecraftHome == null) {
			result = result.replace("$MINECRAFT", new File(".").getAbsolutePath());
		} else {
			result = result.replace("$MINECRAFT", Launch.minecraftHome.getAbsolutePath());
		}

		if ("/".equals(File.separator)) {
			result = result.replaceAll("\\\\", "/");
		} else {
			result = result.replaceAll("/", "\\\\");
		}
		
		return result;
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent evt) {
		HeuristicBlockDetection.start();
		
		if (debugPrintSchematicList) {
			try {
				PrintWriter writer = new PrintWriter("SchematicDebug.txt", "UTF-8");
				writer.println("*** REGISTERED SCHEMATICS ***");
				SchematicRegistry reg = (SchematicRegistry) BuilderAPI.schematicRegistry;
				for (String s : reg.schematicBlocks.keySet()) {
					writer.println(s + " -> " + reg.schematicBlocks.get(s).clazz.getCanonicalName());
				}
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent evt) {
		// Register GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		// Register save handler
		MinecraftForge.EVENT_BUS.register(new EventHandlerBuilders());

		// Standard blocks
		ISchematicRegistry schemes = BuilderAPI.schematicRegistry;
		
		// TODO Fix capitalization errors
		schemes.registerSchematicBlock(Blocks.AIR, SchematicAir.class);

		schemes.registerSchematicBlock(Blocks.SNOW, SchematicIgnore.class);
		schemes.registerSchematicBlock(Blocks.TALLGRASS, SchematicIgnore.class);
		schemes.registerSchematicBlock(Blocks.DOUBLE_PLANT, SchematicIgnore.class);
		schemes.registerSchematicBlock(Blocks.ICE, SchematicIgnore.class);
		schemes.registerSchematicBlock(Blocks.PISTON_HEAD, SchematicIgnore.class);

		schemes.registerSchematicBlock(Blocks.DIRT, SchematicDirt.class);
		schemes.registerSchematicBlock(Blocks.GRASS, SchematicDirt.class);

		schemes.registerSchematicBlock(Blocks.CACTUS, SchematicCactus.class);

		schemes.registerSchematicBlock(Blocks.FARMLAND, SchematicFarmland.class);
		schemes.registerSchematicBlock(Blocks.WHEAT, SchematicSeeds.class, Items.WHEAT_SEEDS);
		schemes.registerSchematicBlock(Blocks.PUMPKIN_STEM, SchematicSeeds.class, Items.PUMPKIN_SEEDS);
		schemes.registerSchematicBlock(Blocks.MELON_STEM, SchematicSeeds.class, Items.MELON_SEEDS);
		schemes.registerSchematicBlock(Blocks.NETHER_WART, SchematicSeeds.class, Items.NETHER_WART);

		schemes.registerSchematicBlock(Blocks.TORCH, SchematicWallSide.class);
		schemes.registerSchematicBlock(Blocks.REDSTONE_TORCH, SchematicWallSide.class);
		schemes.registerSchematicBlock(Blocks.UNLIT_REDSTONE_TORCH, SchematicWallSide.class);

		schemes.registerSchematicBlock(Blocks.TRIPWIRE_HOOK, SchematicTripWireHook.class);

		schemes.registerSchematicBlock(Blocks.SKULL, SchematicSkull.class);

		schemes.registerSchematicBlock(Blocks.LADDER, SchematicRotate.class, BlockLadder.FACING);
		schemes.registerSchematicBlock(Blocks.ACACIA_FENCE_GATE, SchematicRotate.class, BlockFenceGate.FACING);
		schemes.registerSchematicBlock(Blocks.BIRCH_FENCE_GATE, SchematicRotate.class, BlockFenceGate.FACING);
		schemes.registerSchematicBlock(Blocks.DARK_OAK_FENCE_GATE, SchematicRotate.class, BlockFenceGate.FACING);
		schemes.registerSchematicBlock(Blocks.JUNGLE_FENCE_GATE, SchematicRotate.class, BlockFenceGate.FACING);
		schemes.registerSchematicBlock(Blocks.OAK_FENCE_GATE, SchematicRotate.class, BlockFenceGate.FACING);
		schemes.registerSchematicBlock(Blocks.SPRUCE_FENCE_GATE, SchematicRotate.class, BlockFenceGate.FACING);

		// TODO!

		/*schemes.registerSchematicBlock(Blocks.log, SchematicRotate.class, new int[]{8, 4, 8, 4}, true);
		schemes.registerSchematicBlock(Blocks.log2, SchematicRotate.class, new int[]{8, 4, 8, 4}, true);
		schemes.registerSchematicBlock(Blocks.hay_block, SchematicRotate.class, new int[]{8, 4, 8, 4}, true);
		schemes.registerSchematicBlock(Blocks.quartz_block, SchematicRotate.class, new int[]{4, 3, 4, 3}, true);
		schemes.registerSchematicBlock(Blocks.hopper, SchematicRotate.class, new int[]{2, 5, 3, 4}, true);
		schemes.registerSchematicBlock(Blocks.anvil, SchematicRotate.class, new int[]{0, 1, 2, 3}, true);

		schemes.registerSchematicBlock(Blocks.vine, SchematicRotate.class, new int[]{1, 4, 8, 2}, false);*/
		schemes.registerSchematicBlock(Blocks.TRAPDOOR, SchematicRotate.class, BlockTrapDoor.FACING);

		schemes.registerSchematicBlock(Blocks.FURNACE, SchematicRotate.class, BlockFurnace.FACING);
		schemes.registerSchematicBlock(Blocks.LIT_FURNACE, SchematicRotate.class, BlockFurnace.FACING);
		schemes.registerSchematicBlock(Blocks.CHEST, SchematicRotate.class, BlockChest.FACING);
		schemes.registerSchematicBlock(Blocks.DISPENSER, SchematicRotate.class, BlockDispenser.FACING);
		schemes.registerSchematicBlock(Blocks.DROPPER, SchematicRotate.class, BlockDropper.FACING);

		schemes.registerSchematicBlock(Blocks.ENDER_CHEST, SchematicEnderChest.class);


		schemes.registerSchematicBlock(Blocks.WOODEN_BUTTON, SchematicLever.class);
		schemes.registerSchematicBlock(Blocks.STONE_BUTTON, SchematicLever.class);
		schemes.registerSchematicBlock(Blocks.LEVER, SchematicLever.class);

		schemes.registerSchematicBlock(Blocks.STONE, SchematicStone.class);
		schemes.registerSchematicBlock(Blocks.GOLD_ORE, SchematicStone.class);
		schemes.registerSchematicBlock(Blocks.IRON_ORE, SchematicStone.class);
		schemes.registerSchematicBlock(Blocks.COAL_ORE, SchematicStone.class);
		schemes.registerSchematicBlock(Blocks.LAPIS_ORE, SchematicStone.class);
		schemes.registerSchematicBlock(Blocks.DIAMOND_ORE, SchematicStone.class);
		schemes.registerSchematicBlock(Blocks.REDSTONE_ORE, SchematicStone.class);
		schemes.registerSchematicBlock(Blocks.LIT_REDSTONE_ORE, SchematicStone.class);
		schemes.registerSchematicBlock(Blocks.EMERALD_ORE, SchematicStone.class);

		schemes.registerSchematicBlock(Blocks.GRAVEL, SchematicGravel.class);

		schemes.registerSchematicBlock(Blocks.REDSTONE_WIRE, SchematicRedstoneWire.class, new ItemStack(Items.REDSTONE));
		schemes.registerSchematicBlock(Blocks.CAKE, SchematicCustomStack.class, new ItemStack(Items.CAKE));
		schemes.registerSchematicBlock(Blocks.GLOWSTONE, SchematicCustomStack.class, new ItemStack(Blocks.GLOWSTONE));

		schemes.registerSchematicBlock(Blocks.POWERED_REPEATER, SchematicRedstoneDiode.class, Items.REPEATER);
		schemes.registerSchematicBlock(Blocks.UNPOWERED_REPEATER, SchematicRedstoneDiode.class, Items.REPEATER);
		schemes.registerSchematicBlock(Blocks.POWERED_COMPARATOR, SchematicRedstoneDiode.class, Items.COMPARATOR);
		schemes.registerSchematicBlock(Blocks.UNPOWERED_COMPARATOR, SchematicRedstoneDiode.class, Items.COMPARATOR);

		schemes.registerSchematicBlock(Blocks.REDSTONE_LAMP, SchematicRedstoneLamp.class);
		schemes.registerSchematicBlock(Blocks.LIT_REDSTONE_LAMP, SchematicRedstoneLamp.class); // System.out.println("LITTTT BROOO");

		schemes.registerSchematicBlock(Blocks.GLASS_PANE, SchematicGlassPane.class);
		schemes.registerSchematicBlock(Blocks.STAINED_GLASS_PANE, SchematicGlassPane.class);

		schemes.registerSchematicBlock(Blocks.PISTON, SchematicPiston.class);
		schemes.registerSchematicBlock(Blocks.PISTON_EXTENSION, SchematicPiston.class);
		schemes.registerSchematicBlock(Blocks.STICKY_PISTON, SchematicPiston.class);

		schemes.registerSchematicBlock(Blocks.LIT_PUMPKIN, SchematicPumpkin.class);

		schemes.registerSchematicBlock(Blocks.OAK_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.STONE_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.BRICK_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.STONE_BRICK_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.NETHER_BRICK_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.SANDSTONE_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.SPRUCE_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.BIRCH_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.JUNGLE_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.QUARTZ_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.ACACIA_STAIRS, SchematicStairs.class);
		schemes.registerSchematicBlock(Blocks.DARK_OAK_STAIRS, SchematicStairs.class);

		schemes.registerSchematicBlock(Blocks.ACACIA_DOOR, SchematicDoor.class, new ItemStack(Items.ACACIA_DOOR));
		schemes.registerSchematicBlock(Blocks.BIRCH_DOOR, SchematicDoor.class, new ItemStack(Items.BIRCH_DOOR));
		schemes.registerSchematicBlock(Blocks.DARK_OAK_DOOR, SchematicDoor.class, new ItemStack(Items.DARK_OAK_DOOR));
		schemes.registerSchematicBlock(Blocks.JUNGLE_DOOR, SchematicDoor.class, new ItemStack(Items.JUNGLE_DOOR));
		schemes.registerSchematicBlock(Blocks.OAK_DOOR, SchematicDoor.class, new ItemStack(Items.OAK_DOOR));
		schemes.registerSchematicBlock(Blocks.SPRUCE_DOOR, SchematicDoor.class, new ItemStack(Items.SPRUCE_DOOR));
		schemes.registerSchematicBlock(Blocks.IRON_DOOR, SchematicDoor.class, new ItemStack(Items.IRON_DOOR));

		schemes.registerSchematicBlock(Blocks.BED, SchematicBed.class);

		schemes.registerSchematicBlock(Blocks.WALL_SIGN, SchematicSign.class, true);
		schemes.registerSchematicBlock(Blocks.STANDING_SIGN, SchematicSign.class, false);

		schemes.registerSchematicBlock(Blocks.PORTAL, SchematicPortal.class);

		schemes.registerSchematicBlock(Blocks.RAIL, SchematicRail.class);
		schemes.registerSchematicBlock(Blocks.ACTIVATOR_RAIL, SchematicRail.class);
		schemes.registerSchematicBlock(Blocks.DETECTOR_RAIL, SchematicRail.class);
		schemes.registerSchematicBlock(Blocks.GOLDEN_RAIL, SchematicRail.class);

		schemes.registerSchematicBlock(Blocks.FIRE, SchematicFire.class);

		schemes.registerSchematicBlock(Blocks.BEDROCK, SchematicBlockCreative.class);

		schemes.registerSchematicBlock(Blocks.MOB_SPAWNER, SchematicTileCreative.class);

		schemes.registerSchematicBlock(Blocks.GLASS, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.STONE_SLAB, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.DOUBLE_STONE_SLAB, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.WOODEN_SLAB, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.DOUBLE_WOODEN_SLAB, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.STAINED_GLASS, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.ACACIA_FENCE, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.BIRCH_FENCE, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.DARK_OAK_FENCE, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.JUNGLE_FENCE, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.OAK_FENCE, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.SPRUCE_FENCE, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.DAYLIGHT_DETECTOR, SchematicStandalone.class);
		schemes.registerSchematicBlock(Blocks.IRON_BARS, SchematicStandalone.class);

		// Standard entities

		schemes.registerSchematicEntity(EntityMinecartEmpty.class, SchematicMinecart.class, Items.MINECART);
		schemes.registerSchematicEntity(EntityMinecartFurnace.class, SchematicMinecart.class, Items.FURNACE_MINECART);
		schemes.registerSchematicEntity(EntityMinecartTNT.class, SchematicMinecart.class, Items.TNT_MINECART);
		schemes.registerSchematicEntity(EntityMinecartChest.class, SchematicMinecart.class, Items.CHEST_MINECART);
		schemes.registerSchematicEntity(EntityMinecartHopper.class, SchematicMinecart.class, Items.HOPPER_MINECART);

		schemes.registerSchematicEntity(EntityPainting.class, SchematicHanging.class, Items.PAINTING);
		schemes.registerSchematicEntity(EntityItemFrame.class, SchematicHanging.class, Items.ITEM_FRAME);

		// BuildCraft blocks

		schemes.registerSchematicBlock(architectBlock, SchematicRotate.class, BlockBuildCraft.FACING_PROP);
		schemes.registerSchematicBlock(builderBlock, SchematicRotate.class, BlockBuildCraft.FACING_PROP);

		schemes.registerSchematicBlock(markerBlock, SchematicWallSide.class);
		schemes.registerSchematicBlock(pathMarkerBlock, SchematicWallSide.class);
		schemes.registerSchematicBlock(constructionMarkerBlock, SchematicWallSide.class);

		// Factories required to save entities in world

		SchematicFactory.registerSchematicFactory(SchematicBlock.class, new SchematicFactoryBlock());
		SchematicFactory.registerSchematicFactory(SchematicMask.class, new SchematicFactoryMask());
		SchematicFactory.registerSchematicFactory(SchematicEntity.class, new SchematicFactoryEntity());

		BlueprintDeployer.instance = new RealBlueprintDeployer();

		if (BuildCraftCore.loadDefaultRecipes) {
			loadRecipes();
		}

		BuilderProxy.proxy.registerBlockRenderers();
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent evt) {
		templateItem = new ItemBlueprintTemplate();
		templateItem.setUnlocalizedName("templateItem");
		CoreProxy.proxy.registerItem(templateItem);

		blueprintItem = new ItemBlueprintStandard();
		blueprintItem.setUnlocalizedName("blueprintItem");
		CoreProxy.proxy.registerItem(blueprintItem);

		buildToolBlock = new BlockBuildTool ();
		buildToolBlock.setUnlocalizedName("buildToolBlock");
		CoreProxy.proxy.registerBlock(buildToolBlock);

		markerBlock = new BlockMarker();
		CoreProxy.proxy.registerBlock(markerBlock.setUnlocalizedName("markerBlock"));

		pathMarkerBlock = new BlockPathMarker();
		CoreProxy.proxy.registerBlock(pathMarkerBlock.setUnlocalizedName("pathMarkerBlock"));

		constructionMarkerBlock = new BlockConstructionMarker();
		CoreProxy.proxy.registerBlock(constructionMarkerBlock.setUnlocalizedName("constructionMarkerBlock"),
				ItemConstructionMarker.class);

		fillerBlock = new BlockFiller();
		CoreProxy.proxy.registerBlock(fillerBlock.setUnlocalizedName("fillerBlock"));

		builderBlock = new BlockBuilder();
		CoreProxy.proxy.registerBlock(builderBlock.setUnlocalizedName("builderBlock"));

		architectBlock = new BlockArchitect();
		CoreProxy.proxy.registerBlock(architectBlock.setUnlocalizedName("architectBlock"));

		libraryBlock = new BlockBlueprintLibrary();
		CoreProxy.proxy.registerBlock(libraryBlock.setUnlocalizedName("libraryBlock"));

		if (!BuildCraftCore.NONRELEASED_BLOCKS) {
			urbanistBlock = new BlockUrbanist ();
			CoreProxy.proxy.registerBlock(urbanistBlock.setUnlocalizedName("urbanistBlock"));
			CoreProxy.proxy.registerTileEntity(TileUrbanist.class, "net.minecraft.src.builders.TileUrbanist");
		}

		GameRegistry.registerTileEntity(TileMarker.class, "Marker");
		GameRegistry.registerTileEntity(TileFiller.class, "Filler");
		GameRegistry.registerTileEntity(TileBuilder.class, "net.minecraft.src.builders.TileBuilder");
		GameRegistry.registerTileEntity(TileArchitect.class, "net.minecraft.src.builders.TileTemplate");
		GameRegistry.registerTileEntity(TilePathMarker.class, "net.minecraft.src.builders.TilePathMarker");
		GameRegistry.registerTileEntity(TileConstructionMarker.class, "net.minecraft.src.builders.TileConstructionMarker");
		GameRegistry.registerTileEntity(TileBlueprintLibrary.class, "net.minecraft.src.builders.TileBlueprintLibrary");

		SchematicRegistry.INSTANCE.readConfiguration(BuildCraftCore.mainConfiguration);

		if (BuildCraftCore.mainConfiguration.hasChanged()) {
			BuildCraftCore.mainConfiguration.save();
		}

		MinecraftForge.EVENT_BUS.register(this);

		// Create filler registry
		try {
			FillerManager.registry = new FillerRegistry();

			// INIT FILLER PATTERNS
			FillerManager.registry.addPattern(PatternFill.INSTANCE);
			FillerManager.registry.addPattern(new PatternFlatten());
			FillerManager.registry.addPattern(new PatternHorizon());
			FillerManager.registry.addPattern(new PatternClear());
			FillerManager.registry.addPattern(new PatternBox());
			FillerManager.registry.addPattern(new PatternPyramid());
			FillerManager.registry.addPattern(new PatternStairs());
			FillerManager.registry.addPattern(new PatternCylinder());
			FillerManager.registry.addPattern(new PatternFrame());
		} catch (Error error) {
			BCLog.logErrorAPI("Buildcraft", error, IFillerPattern.class);
			throw error;
		}

		StatementManager.registerActionProvider(new BuildersActionProvider());
	}

	public static void loadRecipes() {
		CoreProxy.proxy.addCraftingRecipe(new ItemStack(templateItem, 1), "ppp", "pip", "ppp", 'i',
			"dyeBlack", 'p', Items.PAPER);

		CoreProxy.proxy.addCraftingRecipe(new ItemStack(blueprintItem, 1), "ppp", "pip", "ppp", 'i',
			new ItemStack(Items.DYE, 1, 4), 'p', Items.PAPER);

		CoreProxy.proxy.addCraftingRecipe(new ItemStack(markerBlock, 1), "l ", "r ", 'l',
			new ItemStack(Items.DYE, 1, 4), 'r', Blocks.REDSTONE_TORCH);

		CoreProxy.proxy.addCraftingRecipe(new ItemStack(pathMarkerBlock, 1), "l ", "r ", 'l',
			"dyeGreen", 'r', Blocks.REDSTONE_TORCH);

		CoreProxy.proxy.addCraftingRecipe(new ItemStack(fillerBlock, 1), "btb", "ycy", "gCg", 'b',
			"dyeBlack", 't', markerBlock, 'y', "dyeYellow",
			'c', Blocks.CRAFTING_TABLE, 'g', "gearGold", 'C', Blocks.CHEST);

		CoreProxy.proxy.addCraftingRecipe(new ItemStack(builderBlock, 1), "btb", "ycy", "gCg", 'b',
			"dyeBlack", 't', markerBlock, 'y', "dyeYellow",
			'c', Blocks.CRAFTING_TABLE, 'g', "gearDiamond", 'C', Blocks.CHEST);

		CoreProxy.proxy.addCraftingRecipe(new ItemStack(architectBlock, 1), "btb", "ycy", "gCg", 'b',
			"dyeBlack", 't', markerBlock, 'y', "dyeYellow",
			'c', Blocks.CRAFTING_TABLE, 'g', "gearDiamond", 'C',
			new ItemStack(blueprintItem, 1));

		CoreProxy.proxy.addCraftingRecipe(new ItemStack(libraryBlock, 1), "bbb", "bBb", "bbb", 'b',
			new ItemStack(blueprintItem), 'B', Blocks.BOOKSHELF);
	}

	@Mod.EventHandler
	public void processIMCRequests(FMLInterModComms.IMCEvent event) {
		InterModComms.processIMC(event);
	}

	@Mod.EventHandler
	public void serverStop(FMLServerStoppingEvent event) {
		TilePathMarker.clearAvailableMarkersList();
	}

	/*@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void loadTextures(TextureStitchEvent.Pre evt) {
		if (evt.map.getTextureType() == 0) {
			for (FillerPattern pattern : FillerPattern.patterns.values()) {
				pattern.registerIcon(evt.map);
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void textureHook(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 1) {
			UrbanistToolsIconProvider.INSTANCE.registerIcons(event.map);
		}
	}*/

	@Mod.EventHandler
	public void whiteListAppliedEnergetics(FMLInitializationEvent event) {
		//FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial",
		//		TileMarker.class.getCanonicalName());
		//FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial",
		//		TileFiller.class.getCanonicalName());
		//FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial",
		//		TileBuilder.class.getCanonicalName());
		//FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial",
		//		TileArchitect.class.getCanonicalName());
		//FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial",
		//		TilePathMarker.class.getCanonicalName());
		FMLInterModComms.sendMessage("appliedenergistics2", "whitelist-spatial",
				TileBlueprintLibrary.class.getCanonicalName());
	}
}
