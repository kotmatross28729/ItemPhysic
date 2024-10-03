package com.creativemd.itemphysic;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.itemphysic.config.ItemConfigSystem;
import com.creativemd.itemphysic.list.ItemsWithMetaRegistryBurn;
import com.creativemd.itemphysic.list.ItemsWithMetaRegistryExplosion;
import com.creativemd.itemphysic.list.ItemsWithMetaRegistryFloat;
import com.creativemd.itemphysic.list.ItemsWithMetaRegistrySulfuricAcid;
import com.creativemd.itemphysic.list.ItemsWithMetaRegistryUndestroyable;
import com.creativemd.itemphysic.packet.DropPacket;
import com.creativemd.itemphysic.packet.PickupPacket;
import com.creativemd.itemphysic.physics.ClientPhysic;
import com.creativemd.itemphysic.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.creativemd.itemphysic.ItemPhysic.MODID;
import static com.creativemd.itemphysic.ItemPhysic.NAME;
import static com.creativemd.itemphysic.ItemPhysic.VERSION;

@Mod(modid = MODID, version = VERSION, name = NAME)
public class ItemPhysic {
    public static final String MODID = "itemphysic";
    public static final String NAME = "ItemPhysic";
    public static final String VERSION = "1.2.6" + " kotmatross edition";
    public static final String CLIENTPROXY = "com.creativemd.itemphysic.proxy.ClientProxy";
    public static final String SERVERPROXY = "com.creativemd.itemphysic.proxy.CommonProxy";

    @Mod.Instance(MODID)
    public static ItemPhysic instance;

    @SidedProxy(clientSide = CLIENTPROXY, serverSide = SERVERPROXY)
    public static CommonProxy proxy;

    public static Configuration config;
    public static final Logger logger = LogManager.getLogger();

    public static float rotateSpeed = 1.0F;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        if (!ItemTransformer.isLite) {

            enableItemDespawn = config.getBoolean("enableItemDespawn", "Item", true, "Whether to allow items to despawn after some times. False to disable despawn.");
            despawnItem = config.getInt("despawn","Item",6000, 0, 2147483647, "Number of ticks an item takes to despawn (affected by enableItemDespawn).");
            customPickup = config.getBoolean("customPickup", "Item", false, "Whether to enable a custom pickup mechanic with right click or sneaking (disables auto pickup).");
            customThrow = config.getBoolean("customThrow", "Item", true, "Whether to enable a custom throwing mechanic when you hold the button.");
            showPowerText = config.getBoolean("showPowerText", "Item", true, "Whether to enable a \"Power\" text above HUD");
            disableCactusDamage = config.getBoolean("disableCactusDamage", "Item", true, "Whether to disable cactus damage for items");

            invertBurnList = config.getBoolean("invertBurnList", "listBurn", false, "Whether to invert the burn list (so items in it will be the only ones that able to burn).");
            burnList = config.getStringList("burnList","listBurn", new String[]{
                "minecraft:obsidian",
                "minecraft:netherrack",
                "minecraft:soul_sand",
                "minecraft:glowstone",
                "minecraft:nether_brick",
                "minecraft:nether_brick_fence",
                "minecraft:nether_brick_stairs",
                "minecraft:enchanting_table",
                "minecraft:golden_apple:1",
                "minecraft:bucket",
                "minecraft:water_bucket",
                "minecraft:lava_bucket",
                "minecraft:milk_bucket",
                "minecraft:blaze_rod",
                "minecraft:ghast_tear",
                "minecraft:nether_wart",
                "minecraft:blaze_powder",
                "minecraft:magma_cream",
                "minecraft:fire_charge",
                "minecraft:netherbrick",
                "Thaumcraft:blockCustomOre:2",
                "Thaumcraft:blockCrystal:1",
                "Thaumcraft:ItemShard:1",
                "Thaumcraft:ItemShard:6",
                "Thaumcraft:FocusFire",
                "etfuturum:netherite_scrap",
                "etfuturum:netherite_ingot",
                "etfuturum:netherite_helmet",
                "etfuturum:netherite_chestplate",
                "etfuturum:netherite_leggings",
                "etfuturum:netherite_boots",
                "etfuturum:netherite_pickaxe",
                "etfuturum:netherite_spade",
                "etfuturum:netherite_axe",
                "etfuturum:netherite_hoe",
                "etfuturum:netherite_sword",
                "etfuturum:totem_of_undying",
                "oreTungsten",
                "etfuturum:red_netherbrick",
                "etfuturum:red_netherbrick:1",
                "etfuturum:red_netherbrick:2",
                "etfuturum:ancient_debris",
                "etfuturum:netherite_block",
                "etfuturum:nether_gold_ore",
                "etfuturum:nether_brick_wall",
                "etfuturum:red_nether_brick_wall",
                "etfuturum:red_netherbrick_stairs",
                "etfuturum:red_netherbrick_slab",
                "etfuturum:soul_soil",
                "etfuturum:netherite_stairs",
                "etfuturum:modded_raw_ore_block:9",
                "etfuturum:deepslate_thaumcraft_ore:2",
                "hbm:item.ingot_schrabidium",
                "hbm:item.ingot_schrabidate",
            }, "List of items that won't burn in lava or fire. See documentation on github");

            invertFloatList = config.getBoolean("invertFloatList", "listFloat", false, "Whether to invert the float list (so items in it won't be able to float).");
            floatList = config.getStringList("floatList","listFloat", new String[]{
                "minecraft:bedrock:fluid.tile.lava",
                "minecraft:netherrack:fluid.tile.lava",
                "minecraft:glowstone:fluid.tile.lava",
                "minecraft:nether_brick:fluid.tile.lava",
                "minecraft:nether_brick_fence:fluid.tile.lava",
                "minecraft:nether_brick_stairs:fluid.tile.lava",
                "minecraft:dragon_egg:fluid.tile.lava",
                "minecraft:golden_apple:1:fluid.tile.lava",
                "minecraft:blaze_rod:fluid.tile.lava",
                "minecraft:nether_wart:fluid.tile.lava",
                "minecraft:blaze_powder:fluid.tile.lava",
                "minecraft:magma_cream:fluid.tile.lava",
                "minecraft:fire_charge:fluid.tile.lava",
                "minecraft:netherbrick:fluid.tile.lava",
                "Thaumcraft:blockCustomOre:2:fluid.tile.lava",
                "Thaumcraft:blockCrystal:1:fluid.tile.lava",
                "Thaumcraft:ItemShard:1:fluid.tile.lava",
                "Thaumcraft:ItemShard:6:fluid.tile.lava",
                "Thaumcraft:FocusFire:fluid.tile.lava",
                "etfuturum:netherite_scrap:fluid.tile.lava",
                "etfuturum:netherite_ingot:fluid.tile.lava",
                "etfuturum:netherite_helmet:fluid.tile.lava",
                "etfuturum:netherite_chestplate:fluid.tile.lava",
                "etfuturum:netherite_leggings:fluid.tile.lava",
                "etfuturum:netherite_boots:fluid.tile.lava",
                "etfuturum:netherite_pickaxe:fluid.tile.lava",
                "etfuturum:netherite_spade:fluid.tile.lava",
                "etfuturum:netherite_axe:fluid.tile.lava",
                "etfuturum:netherite_hoe:fluid.tile.lava",
                "etfuturum:netherite_sword:fluid.tile.lava",
                "etfuturum:red_netherbrick:fluid.tile.lava",
                "etfuturum:red_netherbrick:1:fluid.tile.lava",
                "etfuturum:red_netherbrick:2:fluid.tile.lava",
                "etfuturum:ancient_debris:fluid.tile.lava",
                "etfuturum:nether_gold_ore:fluid.tile.lava",
                "etfuturum:nether_brick_wall:fluid.tile.lava",
                "etfuturum:red_nether_brick_wall:fluid.tile.lava",
                "etfuturum:red_netherbrick_stairs:fluid.tile.lava",
                "etfuturum:red_netherbrick_slab:fluid.tile.lava",
                "etfuturum:deepslate_thaumcraft_ore:2:fluid.tile.lava",
                "minecraft:stick",
                "plankWood",
                "logWood",
                "blockCloth",
                "stairWood",
                "minecraft:wooden_pickaxe:true",
                "minecraft:wooden_shovel:true",
                "minecraft:wooden_sword:true",
                "minecraft:wooden_axe:true",
                "minecraft:wooden_hoe:true",
                "minecraft:hay_block",
            }, "List of items that will float in fluids. See documentation on github");

            invertExplosionList = config.getBoolean("invertExplosionList", "listExplosion", false, "Whether to invert the explosion list (so items in it will be the only ones that able to explode).");
            explosionList = config.getStringList("explosionList","listExplosion", new String[]{
                "minecraft:obsidian",
                "Thaumcraft:ItemShard:6",
                "etfuturum:netherite_scrap",
                "etfuturum:netherite_ingot",
                "etfuturum:netherite_helmet",
                "etfuturum:netherite_chestplate",
                "etfuturum:netherite_leggings",
                "etfuturum:netherite_boots",
                "etfuturum:netherite_pickaxe",
                "etfuturum:netherite_spade",
                "etfuturum:netherite_axe",
                "etfuturum:netherite_hoe",
                "etfuturum:netherite_sword",
                "etfuturum:totem_of_undying",
                "etfuturum:ancient_debris",
                "etfuturum:netherite_block",
                "etfuturum:netherite_stairs",
                "minecraft:nether_star",
            }, "List of items that are resistant to explosions");
            invertUndestroyableList = config.getBoolean("invertUndestroyableList", "listUndestroyable", false, "Whether to invert the undestroyable list (so items in it will be the only ones that can be destroyed).");
            undestroyableList = config.getStringList("undestroyableList","listUndestroyable", new String[]{
                "minecraft:bedrock",
                "minecraft:dragon_egg",
                "minecraft:command_block",
                "minecraft:golden_apple:1",
            }, "List of items that are invulnerable to any type of damage");

            invertSulfuricAcidList = config.getBoolean("invertSulfuricAcidList", "listSulfuricAcid", false, "Whether to invert the sulfuric acid list (so items in it will be the only ones that can dissolve in sulfuric acid).");
            sulfuricAcidList = config.getStringList("sulfuricAcidList","listSulfuricAcid", new String[]{
                "minecraft:glass",
                "gemQuartz",
                "blockQuartz",
                "ingotGold",
                "ingotGold198",
                "billetGold198",
                "nuggetGold",
                "nuggetGold198",
                "blockGold",
                "pressurePlateGold",
                "minecraft:golden_sword",
                "minecraft:golden_shovel",
                "minecraft:golden_pickaxe",
                "minecraft:golden_axe",
                "minecraft:golden_hoe",
                "minecraft:golden_helmet",
                "minecraft:golden_chestplate",
                "minecraft:golden_leggings",
                "minecraft:golden_boots",
                "minecraft:golden_apple",
                "minecraft:golden_carrot",
                "Thaumcraft:WandCap:1",
                "Thaumcraft:ItemResource:18",
                "Thaumcraft:ItemNugget:31",
                "Thaumcraft:ArcaneDoorKey:1",
                "dustGold",
                "dustGold198",
                "gearGold",
                "rawGold",
                "hbm:tile.ladder_gold",
                "hbm:tile.capacitor_gold",
                "plateTripleGold",
                "wireFineGold",
                "wireDenseGold",
                "hbm:item.crystal_gold",
                "hbm:item.coil_gold_torus",
                "hbm:item.coil_gold",
                "plateGold",
                "ingotAnyHardPlastic",
            }, "List of items that are resistant to sulfuric acid from hbm's ntm");
        }

        rotateSpeed = config.getFloat("rotateSpeed", "Item", 1.0F, 0, 100, "Speed of the item rotation.");
        config.save();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerEvents();
        proxy.init(this);
        if (!ItemTransformer.isLite) {
            MinecraftForge.EVENT_BUS.register(new EventHandler());
            FMLCommonHandler.instance().bus().register(new EventHandler());
            initFull();
        } else {
            MinecraftForge.EVENT_BUS.register(new EventHandlerLite());
            FMLCommonHandler.instance().bus().register(new EventHandlerLite());
        }
    }
    @Optional.Method(modid = "creativecore")
    public static void initFull() {
        CreativeCorePacket.registerPacket(DropPacket.class, "IPDrop");
        CreativeCorePacket.registerPacket(PickupPacket.class, "IPPick");

        try {
            if (!ItemTransformer.isLite && Loader.isModLoaded("ingameconfigmanager")) ItemConfigSystem.loadConfig();
        } catch(Exception e) {}
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void onRender(TickEvent.RenderTickEvent evt) {
        ClientPhysic.tick = System.nanoTime();
    }

    //public static boolean isTCLoaded = false;
    public static boolean isHBMLoaded = false;

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
//        if(Loader.isModLoaded("Thaumcraft")){
//            isTCLoaded = true;
//        }
        if(Loader.isModLoaded("hbm")){
            isHBMLoaded = true;
        }

        //This approach also acts like a hash function, initializing 2 lists at a late stage of loading so that the lists don't have to be checked constantly
        for (String itemName : burnList) {
            String modId;
            String itemNameOnly;
            int metadata = 0;
            boolean ignoremeta = false;

            String[] parts = itemName.split(":");
            if (parts.length >= 2) {
                modId = parts[0];
                itemNameOnly = parts[1];
                if (parts.length == 3) {
                    try {
                        metadata = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        ignoremeta = Boolean.parseBoolean(parts[2]);
                    }
                }
                Item item = GameRegistry.findItem(modId, itemNameOnly);
                if (item != null) {
                    ItemsWithMetaRegistryBurn.ItemWithMetaBurn Item = new ItemsWithMetaRegistryBurn.ItemWithMetaBurn(item, metadata, ignoremeta);
                    ItemsWithMetaRegistryBurn.BurnItems.add(Item);
                }
            } else if(parts.length == 1) {
                List<String> oredictNames = Arrays.asList(OreDictionary.getOreNames());
                if (oredictNames.contains(itemName)) {
                    for (ItemStack oreStack : OreDictionary.getOres(itemName)) {
                        ItemsWithMetaRegistryBurn.ItemWithMetaBurn Item = new ItemsWithMetaRegistryBurn.ItemWithMetaBurn(oreStack.getItem(), oreStack.getItemDamage(), ignoremeta);
                        ItemsWithMetaRegistryBurn.BurnItems.add(Item);
                    }
                }
            }
        }
        for (String itemName : floatList) {
            String modId;
            String itemNameOnly;
            int metadata = 0;
            boolean ignoremeta = false;
            List<String> liquidsList = new ArrayList<>();
            liquidsList.add("fluid.tile.water"); // default

            String[] parts = itemName.split(":");
            if (parts.length >= 2) {
                modId = parts[0];
                itemNameOnly = parts[1];
                if (parts.length == 3) {
                    try {
                        metadata = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        ignoremeta = Boolean.parseBoolean(parts[2]);
                    } if(!ignoremeta && metadata == 0) {
                        liquidsList.set(0, parts[2]); //assert that ignoremeta missing (I assure you, no one will write :false)
                    }
                } else if(parts.length > 3) {
                    try {
                        metadata = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        ignoremeta = Boolean.parseBoolean(parts[2]);
                    }
                    liquidsList.addAll(Arrays.asList(parts).subList(3, parts.length));
                }
                Item item = GameRegistry.findItem(modId, itemNameOnly);
                if (item != null) {
                    String[] liquidsArray = liquidsList.toArray(new String[0]);
                    ItemsWithMetaRegistryFloat.ItemWithMetaFloat Item = new ItemsWithMetaRegistryFloat.ItemWithMetaFloat(item, metadata, ignoremeta, liquidsArray);
                    ItemsWithMetaRegistryFloat.FloatItems.add(Item);
                }
            } else if(parts.length == 1) {
                List<String> oredictNames = Arrays.asList(OreDictionary.getOreNames());
                if (oredictNames.contains(itemName)) {
                    for (ItemStack oreStack : OreDictionary.getOres(itemName)) {
                        String[] liquidsArray = liquidsList.toArray(new String[0]);
                        if(oreStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                            ItemsWithMetaRegistryFloat.ItemWithMetaFloat Item = new ItemsWithMetaRegistryFloat.ItemWithMetaFloat(oreStack.getItem(), oreStack.getItemDamage(), true, liquidsArray);
                            ItemsWithMetaRegistryFloat.FloatItems.add(Item);
                        } else {
                            ItemsWithMetaRegistryFloat.ItemWithMetaFloat Item = new ItemsWithMetaRegistryFloat.ItemWithMetaFloat(oreStack.getItem(), oreStack.getItemDamage(), ignoremeta, liquidsArray);
                            ItemsWithMetaRegistryFloat.FloatItems.add(Item);
                        }
                    }
                }
            }
        }
        for (String itemName : explosionList) {
            String modId;
            String itemNameOnly;
            int metadata = 0;
            boolean ignoremeta = false;

            String[] parts = itemName.split(":");
            if (parts.length >= 2) {
                modId = parts[0];
                itemNameOnly = parts[1];
                if (parts.length == 3) {
                    try {
                        metadata = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        ignoremeta = Boolean.parseBoolean(parts[2]);
                    }
                }
                Item item = GameRegistry.findItem(modId, itemNameOnly);
                if (item != null) {
                    ItemsWithMetaRegistryExplosion.ItemsWithMetaExplosion Item = new ItemsWithMetaRegistryExplosion.ItemsWithMetaExplosion(item, metadata, ignoremeta);
                    ItemsWithMetaRegistryExplosion.ExplosionItems.add(Item);
                }
            } else if(parts.length == 1) {
                List<String> oredictNames = Arrays.asList(OreDictionary.getOreNames());
                if (oredictNames.contains(itemName)) {
                    for (ItemStack oreStack : OreDictionary.getOres(itemName)) {
                        ItemsWithMetaRegistryExplosion.ItemsWithMetaExplosion Item = new ItemsWithMetaRegistryExplosion.ItemsWithMetaExplosion(oreStack.getItem(), oreStack.getItemDamage(), ignoremeta);
                        ItemsWithMetaRegistryExplosion.ExplosionItems.add(Item);
                    }
                }
            }
        }
        for (String itemName : undestroyableList) {
            String modId;
            String itemNameOnly;
            int metadata = 0;
            boolean ignoremeta = false;

            String[] parts = itemName.split(":");
            if (parts.length >= 2) {
                modId = parts[0];
                itemNameOnly = parts[1];
                if (parts.length == 3) {
                    try {
                        metadata = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        ignoremeta = Boolean.parseBoolean(parts[2]);
                    }
                }
                Item item = GameRegistry.findItem(modId, itemNameOnly);
                if (item != null) {
                    ItemsWithMetaRegistryUndestroyable.ItemWithMetaUndestroyable Item = new ItemsWithMetaRegistryUndestroyable.ItemWithMetaUndestroyable(item, metadata, ignoremeta);
                    ItemsWithMetaRegistryUndestroyable.UndestroyableItems.add(Item);
                }
            } else if(parts.length == 1) {
                List<String> oredictNames = Arrays.asList(OreDictionary.getOreNames());
                if (oredictNames.contains(itemName)) {
                    for (ItemStack oreStack : OreDictionary.getOres(itemName)) {
                        ItemsWithMetaRegistryUndestroyable.ItemWithMetaUndestroyable Item = new ItemsWithMetaRegistryUndestroyable.ItemWithMetaUndestroyable(oreStack.getItem(), oreStack.getItemDamage(), ignoremeta);
                        ItemsWithMetaRegistryUndestroyable.UndestroyableItems.add(Item);
                    }
                }
            }
        }

        if(isHBMLoaded) {
            for (String itemName : sulfuricAcidList) {
                String modId;
                String itemNameOnly;
                int metadata = 0;
                boolean ignoremeta = false;

                String[] parts = itemName.split(":");
                if (parts.length >= 2) {
                    modId = parts[0];
                    itemNameOnly = parts[1];
                    if (parts.length == 3) {
                        try {
                            metadata = Integer.parseInt(parts[2]);
                        } catch (NumberFormatException e) {
                            ignoremeta = Boolean.parseBoolean(parts[2]);
                        }
                    }
                    Item item = GameRegistry.findItem(modId, itemNameOnly);
                    if (item != null) {
                        ItemsWithMetaRegistrySulfuricAcid.ItemsWithMetaSulfuricAcid Item = new ItemsWithMetaRegistrySulfuricAcid.ItemsWithMetaSulfuricAcid(item, metadata, ignoremeta);
                        ItemsWithMetaRegistrySulfuricAcid.SulfuricAcidItems.add(Item);
                    }
                } else if (parts.length == 1) {
                    List<String> oredictNames = Arrays.asList(OreDictionary.getOreNames());
                    if (oredictNames.contains(itemName)) {
                        for (ItemStack oreStack : OreDictionary.getOres(itemName)) {
                            ItemsWithMetaRegistrySulfuricAcid.ItemsWithMetaSulfuricAcid Item = new ItemsWithMetaRegistrySulfuricAcid.ItemsWithMetaSulfuricAcid(oreStack.getItem(), oreStack.getItemDamage(), ignoremeta);
                            ItemsWithMetaRegistrySulfuricAcid.SulfuricAcidItems.add(Item);
                        }
                    }
                }
            }
        }


    }
    public static boolean enableItemDespawn;
    public static int despawnItem;
    public static boolean customPickup;
    public static boolean customThrow;
    public static boolean showPowerText;
    public static boolean disableCactusDamage;

    public static boolean invertBurnList;
    public static String[] burnList;

    public static boolean invertFloatList;
    public static String[] floatList;

    public static boolean invertExplosionList;
    public static String[] explosionList;

    public static boolean invertUndestroyableList;
    public static String[] undestroyableList;

    public static boolean invertSulfuricAcidList;
    public static String[] sulfuricAcidList;


}
