package org.wyldmods.kitchencraft.foods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.common.CommonProxy;
import org.wyldmods.kitchencraft.foods.common.block.KCBlocks;
import org.wyldmods.kitchencraft.foods.common.command.CommandKCFoods;
import org.wyldmods.kitchencraft.foods.common.config.ConfigurationHandler;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;

import com.enderio.core.IEnderMod;
import com.enderio.core.common.Lang;
import com.enderio.core.common.compat.CompatRegistry;
import com.enderio.core.common.util.CreativeTabsCustom;
import com.enderio.core.common.util.RegisterTime;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid=Reference.MOD_ID_FOODS, name=Reference.MOD_NAME_FOODS, version=Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class KitchenCraftFoods implements IEnderMod
{
    @Instance(Reference.MOD_ID_FOODS)
    public static KitchenCraftFoods instance;
        
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS_FOODS, serverSide = Reference.SERVER_PROXY_CLASS_FOODS)
    public static CommonProxy proxy;

    public static CreativeTabsCustom tab = new CreativeTabsCustom("tabKC.foods");

    public static final Logger logger = LogManager.getLogger(Reference.MOD_NAME_FOODS);
    
    public static final Lang lang = new Lang("kc");
    
    public static int renderIDCrop, renderIDGrass;
    
    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        FMLInterModComms.sendMessage(Reference.MOD_ID_MACHINES, "handshake", "");
        
        ConfigurationHandler.preInit(event.getSuggestedConfigurationFile());

        KCItems.preInit();
        KCBlocks.preInit();
                        
        proxy.initRenderers();
        
        FMLInterModComms.sendMessage("Waila", "register", "org.wyldmods.kitchencraft.foods.common.compat.WailaCompat.load");
        CompatRegistry.INSTANCE.registerCompat(RegisterTime.POSTINIT, "org.wyldmods.kitchencraft.foods.common.compat.NEICompat", "NotEnoughItems");
        CompatRegistry.INSTANCE.registerCompat(RegisterTime.INIT, "org.wyldmods.kitchencraft.foods.common.compat.EnderIOCompat", "EnderIO");
    }
    
    @EventHandler
    public static void init(FMLInitializationEvent event)
    {
        ConfigurationHandler.init();
        KCItems.init();
    }
    
    @EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
        ConfigurationHandler.postInit();
    }
    
    @EventHandler
    public static void onServerStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandKCFoods());
    }

    @Override
    public String modid()
    {
        return Reference.MOD_ID_FOODS;
    }

    @Override
    public String name()
    {
        return Reference.MOD_NAME_FOODS;
    }

    @Override
    public String version()
    {
        return Reference.VERSION;
    }
}
