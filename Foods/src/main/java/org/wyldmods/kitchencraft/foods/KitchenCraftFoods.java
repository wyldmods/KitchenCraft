package org.wyldmods.kitchencraft.foods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.common.CommonProxy;
import org.wyldmods.kitchencraft.foods.common.block.KCBlocks;
import org.wyldmods.kitchencraft.foods.common.command.CommandKCFoods;
import org.wyldmods.kitchencraft.foods.common.compat.EnderIOCompat;
import org.wyldmods.kitchencraft.foods.common.compat.NEICompat;
import org.wyldmods.kitchencraft.foods.common.config.ConfigurationHandler;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;

import tterrag.core.common.Handlers;
import tterrag.core.common.Lang;
import tterrag.core.common.compat.CompatabilityRegistry;
import tterrag.core.common.util.CreativeTabsCustom;
import tterrag.core.common.util.RegisterTime;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid=Reference.MOD_ID_FOODS, name=Reference.MOD_NAME_FOODS, version=Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class KitchenCraftFoods
{
    @Instance(Reference.MOD_ID_FOODS)
    public static KitchenCraftFoods instance;
        
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS_FOODS, serverSide = Reference.SERVER_PROXY_CLASS_FOODS)
    public static CommonProxy proxy;

    public static CreativeTabsCustom tab = new CreativeTabsCustom("tabKC.foods");

    public static final Logger logger = LogManager.getLogger(Reference.MOD_NAME_FOODS);
    
    public static final Lang lang = new Lang("kc");
    
    public static int renderIDCrop;
    
    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.preInit(event.getSuggestedConfigurationFile());

        KCItems.preInit();
        KCBlocks.preInit();
                
        ConfigurationHandler.init();
        
        KCItems.init();
        
        Handlers.addPackage("org.wyldmods");
                
        proxy.initRenderers();
        
        FMLInterModComms.sendMessage("Waila", "register", "org.wyldmods.kitchencraft.foods.common.compat.WailaCompat.load");
        CompatabilityRegistry.instance().registerCompat("NotEnoughItems", RegisterTime.INIT, NEICompat.class);
        CompatabilityRegistry.instance().registerCompat("EnderIO", RegisterTime.INIT, EnderIOCompat.class);
    }
    
    @EventHandler
    public static void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandKCFoods());
    }
}
