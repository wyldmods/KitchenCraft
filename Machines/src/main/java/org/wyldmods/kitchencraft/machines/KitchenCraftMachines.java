package org.wyldmods.kitchencraft.machines;

import net.minecraft.block.Block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.client.gui.GuiHandlerKC;
import org.wyldmods.kitchencraft.machines.common.CommonProxy;
import org.wyldmods.kitchencraft.machines.common.block.KCBlocks;

import tterrag.core.common.util.CreativeTabsCustom;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID_MACHINES, name = Reference.MOD_NAME_MACHINES, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class KitchenCraftMachines
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
        proxy.initRenderers();

        KCBlocks.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandlerKC());
    }

    public static boolean loadRF()
    {
        if (!rfCheckLoaded)
        {
            try
            {
                Class.forName("cofh.api.energy.IEnergyHandler");
                loadRF = true;
                KitchenCraftMachines.logger.info("RF API found, allowing RF-only features.");
            }
            catch (ClassNotFoundException e)
            {
                KitchenCraftMachines.logger.info("RF API not found, disabling RF-only features.");
            }
            finally
            {
                rfCheckLoaded = true;
            }
        }

        return loadRF;
    }
}
