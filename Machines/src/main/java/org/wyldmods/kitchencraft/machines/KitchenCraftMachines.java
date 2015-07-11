package org.wyldmods.kitchencraft.machines;

import java.io.File;

import net.minecraft.block.Block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.client.gui.GuiHandlerKC;
import org.wyldmods.kitchencraft.machines.common.CommonProxy;
import org.wyldmods.kitchencraft.machines.common.block.KCBlocks;
import org.wyldmods.kitchencraft.machines.common.config.ConfigHandler;

import com.enderio.core.IEnderMod;
import com.enderio.core.common.util.CreativeTabsCustom;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID_MACHINES, name = Reference.MOD_NAME_MACHINES, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class KitchenCraftMachines implements IEnderMod
{
    @Instance(Reference.MOD_ID_MACHINES)
    public static KitchenCraftMachines instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS_MACHINES, serverSide = Reference.SERVER_PROXY_CLASS_MACHINES)
    public static CommonProxy proxy;

    public static CreativeTabsCustom tab = new CreativeTabsCustom("tabKC.machines");

    public static int renderIDPot;
    public static Block pot;

    public static final Logger logger = LogManager.getLogger(Reference.MOD_NAME_MACHINES);

    private static boolean rfCheckLoaded = false;
    private static boolean loadRF = false;

    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        ConfigHandler.INSTANCE.initialize(new File(event.getSuggestedConfigurationFile().getParent() + "/KitchenCraft/" + event.getSuggestedConfigurationFile().getName()));
        
        proxy.initRenderers();

        KCBlocks.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandlerKC());
    }

    public static boolean loadRF()
    {
        if (!rfCheckLoaded)
        {
            loadRF = ModAPIManager.INSTANCE.hasAPI("CoFHAPI|energy");
            System.out.println("Initialized RF value to " + loadRF);
            rfCheckLoaded = true;
        }

        System.out.println("Checking RF: " + loadRF);

        return loadRF;
    }

    @EventHandler
    public void onIMC(IMCEvent event)
    {
        for (IMCMessage message : event.getMessages())
        {
            if (message.getSender().equals(Reference.MOD_ID_FOODS) && "handshake".equalsIgnoreCase(message.key))
            {
                LogManager.getLogger(Reference.MOD_ID_FOODS).info("Yo, KitchenCraft Machines...WAZZUUUUUP!");
                logger.info("WAZZUUUUUUP!");
            }
        }
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
