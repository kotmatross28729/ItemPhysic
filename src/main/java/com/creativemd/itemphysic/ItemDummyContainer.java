package com.creativemd.itemphysic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;

import net.minecraft.util.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.itemphysic.config.ItemConfigSystem;
import com.creativemd.itemphysic.packet.DropPacket;
import com.creativemd.itemphysic.packet.PickupPacket;
import com.creativemd.itemphysic.physics.ClientPhysic;
import com.creativemd.itemphysic.physics.ServerPhysic;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDummyContainer extends DummyModContainer {

	// We define the mod properties
	public static final String MODID = "itemphysic";
	public static final String NAME = "ItemPhysic";
	public static final String VERSION = "1.1.6" + " kotmatross edition";
	public static final String DESCRIPTION = "A minecraft mod that adds physics to thrown items.";
	public static final String CREDITS = "CreativeMD";
	public static final String URL = "";
	public static final boolean LOGO = false;

	public ItemDummyContainer() {

		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = MODID;
		meta.name = NAME;
		meta.version = VERSION; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
		meta.credits = CREDITS;
		meta.authorList = Arrays.asList(EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD + "CreativeMD",
										EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + "HRudyPlayZ");
		meta.description = DESCRIPTION;
		meta.url = URL;
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void modConstruction(FMLConstructionEvent evt){}

	@Subscribe
	public void init(FMLInitializationEvent evt) {

		if (!ItemTransformer.isLite) {
			MinecraftForge.EVENT_BUS.register(new EventHandler());
			FMLCommonHandler.instance().bus().register(new EventHandler());
			initFull();
		} else {
			MinecraftForge.EVENT_BUS.register(new EventHandlerLite());
			FMLCommonHandler.instance().bus().register(new EventHandlerLite());
		}
	}

	@Method(modid = "creativecore")
	public static void initFull() {
		CreativeCorePacket.registerPacket(DropPacket.class, "IPDrop");
		CreativeCorePacket.registerPacket(PickupPacket.class, "IPPick");

		try {
			if (!ItemTransformer.isLite && Loader.isModLoaded("ingameconfigmanager")) ItemConfigSystem.loadConfig();
		} catch(Exception e) {}
	}

	public static Configuration config;
	public static float rotateSpeed = 1.0F;

	@Subscribe
	public void preInit(FMLPreInitializationEvent evt) {
		// The following overrides the mcmod.info file!
		// Adapted from Jabelar's Magic Beans:
		// https://github.com/jabelar/MagicBeans-1.7.10/blob/e48456397f9c6c27efce18e6b9ad34407e6bc7c7/src/main/java/com/blogspot/jabelarminecraft/magicbeans/MagicBeans.java

		// stops Forge from complaining about missing mcmod.info (in case i forget it).
		evt.getModMetadata().autogenerated = false;

		// Mod name
		evt.getModMetadata().name = EnumChatFormatting.RED + NAME;

		// Mod version
		evt.getModMetadata().version = EnumChatFormatting.GRAY + "" + EnumChatFormatting.BOLD + VERSION;

		// Mod credits
		evt.getModMetadata().credits = EnumChatFormatting.BOLD + CREDITS;

		// Mod URL
		evt.getModMetadata().url = EnumChatFormatting.GRAY + URL;

		// Mod description
		evt.getModMetadata().description = EnumChatFormatting.GRAY + DESCRIPTION;

		// Mod logo
		if (LOGO) evt.getModMetadata().logoFile = "title.png";


		config = new Configuration(evt.getSuggestedConfigurationFile());
		config.load();

		if (!ItemTransformer.isLite) {
			enableItemDespawn = config.getBoolean("enableItemDespawn", "Item", true, "Whether to allow items to despawn after some times");
			despawnItem = config.getInt("despawn","Item",6000, 0, 2147483647, "Number of ticks an item takes to despawn (affected by enableItemDespawn).");
			customPickup = config.getBoolean("customPickup", "Item", false, "Whether to enable a custom pickup mechanic with right click or sneaking (disables auto pickup).");
			customThrow = config.getBoolean("customThrow", "Item", true, "Whether to enable a custom throwing mechanic when you hold the button.");

			invertBurnList = config.getBoolean("invertBurnList", "listBurn", false, "Whether to invert the burn list (so items in it will be the only ones to burn).");
			burnList = config.getStringList("burnList","listBurn", new String[]{
					"minecraft:blaze_rod",
					"minecraft:fire_charge",
					"minecraft:bucket",
					"minecraft:lava_bucket",
					"minecraft:magma_cream",
					"minecraft:netherrack",
					"minecraft:soul_sand",
			}, "List of items that won't burn in lava or fire.");

			invertFloatList = config.getBoolean("invertFloatList", "listFloat", false, "Whether to invert the float list (so items in it won't be able to float).");
			floatList = config.getStringList("floatList","listFloat", new String[]{
					"minecraft:blaze_rod",
					"minecraft:stick",
					"minecraft:wooden_planks",
					"minecraft:log",
					"minecraft:log2",
					"minecraft:wooden_pickaxe",
					"minecraft:wooden_shovel",
					"minecraft:wooden_sword",
					"minecraft:wooden_axe",
					"minecraft:wooden_hoe"
			}, "List of items that will float in fluids such as lava or water.");
		}

		rotateSpeed = config.getFloat("rotateSpeed", "Item", 1.0F, 0, 100, "Speed of the item rotation.");
		config.save();
		ServerPhysic.loadItemList();
	}

	@Subscribe
	@SideOnly(Side.CLIENT)
	public void onRender(RenderTickEvent evt) {
		ClientPhysic.tick = System.nanoTime();
	}

	@Subscribe
	public void postInit(FMLPostInitializationEvent evt) {}

	public static Logger log = LogManager.getLogger(ItemDummyContainer.MODID); // Creates the debug log function.

	public static boolean enableItemDespawn;
	public static int despawnItem;
	public static boolean customPickup;
	public static boolean customThrow;

	public static boolean invertBurnList;
	public static String[] burnList;

	public static boolean invertFloatList;
	public static String[] floatList;

}
