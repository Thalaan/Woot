package ipsis;

import ipsis.woot.command.CommandWoot;
import ipsis.woot.command.WootCommand;
import ipsis.woot.event.HandlerRegistryEvent;
import ipsis.woot.handler.ConfigHandler;
import ipsis.woot.init.ModBlocks;
import ipsis.woot.init.ModEnchantments;
import ipsis.woot.init.ModOreDictionary;
import ipsis.woot.manager.*;
import ipsis.woot.manager.loot.LootTableManager;
//import ipsis.woot.plugins.bloodmagic.BloodMagic;
import ipsis.woot.plugins.imc.EnderIO;
import ipsis.woot.proxy.CommonProxy;
import ipsis.woot.reference.Files;
import ipsis.woot.reference.Reference;
import ipsis.woot.tileentity.multiblock.MobFactoryMultiblockLogic;
import ipsis.woot.tileentity.ng.configuration.IMobCost;
import ipsis.woot.tileentity.ng.configuration.IWootConfiguration;
import ipsis.woot.tileentity.ng.configuration.MobXPManager;
import ipsis.woot.tileentity.ng.configuration.WootConfigurationManager;
import ipsis.woot.tileentity.ng.loot.*;
import ipsis.woot.tileentity.ng.loot.schools.SkyBoxSchool;
import ipsis.woot.tileentity.ng.spawning.EntitySpawner;
import ipsis.woot.tileentity.ng.spawning.IEntitySpawner;
import ipsis.woot.util.DebugSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class Woot {

    @Mod.Instance(Reference.MOD_ID)
    public static Woot instance;
    public static MobRegistry mobRegistry = new MobRegistry();
    public static Random RANDOM = new Random();
    public static LootTableManager LOOT_TABLE_MANAGER = new LootTableManager();
    public static boolean devMode = false;
    public static IWootConfiguration wootConfiguration = new WootConfigurationManager();
    public static ILootGeneration lootGeneration = new LootGeneration();
    public static ILootLearner lootLearner = new SkyBoxSchool();
    public static ILootRepository lootRepository = new LootRepository();
    public static IMobCost mobCosting = new MobXPManager();
    public static IEntitySpawner entitySpawner = new EntitySpawner();
    public static DebugSetup debugSetup = new DebugSetup();

    // TODO fix this nonsense
    public static MobFactoryMultiblockLogic multiblockLogic = new MobFactoryMultiblockLogic();

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static CreativeTabs tabWoot = new CreativeTabs(Reference.MOD_ID) {
        @Override
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(ModBlocks.blockFactory));
        }
    };

    public Woot() {

        MinecraftForge.EVENT_BUS.register(new HandlerRegistryEvent());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        proxy.preInit();
        Files.init(event);

        ConfigHandler.init(Files.configFile);

        FMLInterModComms.sendMessage("Waila", "register", "ipsis.woot.plugins.waila.WailaDataProviderWoot.callbackRegister");
        EnderIO.loadRecipes();

        ModOreDictionary.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.init();

//        if (Loader.isModLoaded(BloodMagic.BM_MODID))
//            BloodMagic.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.postInit();
        ModEnchantments.postInit();
        lootGeneration.initialise();;
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {

        lootRepository.loadFromFile(Files.getLootFile());
        event.registerServerCommand(new CommandWoot());
        event.registerServerCommand(new WootCommand());
    }

    @Mod.EventHandler
    public void serverStop(FMLServerStoppingEvent event) {

        lootRepository.saveToFile(Files.getLootFile());
    }
}
