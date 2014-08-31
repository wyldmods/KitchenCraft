package org.wyldmods.kitchencraft.machines;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.client.gui.GuiHandlerKC;
import org.wyldmods.kitchencraft.machines.common.CommonProxy;
import org.wyldmods.kitchencraft.machines.common.block.KCBlocks;
import org.wyldmods.kitchencraft.machines.common.compat.RFCompat;

import tterrag.core.common.compat.CompatabilityRegistry;
import tterrag.core.common.util.CreativeTabsCustom;
import tterrag.core.common.util.RegisterTime;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid=Reference.MOD_ID_MACHINES, name=Reference.MOD_NAME_MACHINES, version=Reference.VERSION, dependencies="required-after:ttCore@1.7.10-0.0.2-5,)")
public class KitchenCraftMachines
{
    @Instance(Reference.MOD_ID_MACHINES)
    public static KitchenCraftMachines instance;
    
    @SidedProxy(clientSide=Reference.CLIENT_PROXY_CLASS_MACHINES, serverSide=Reference.SERVER_PROXY_CLASS_MACHINES)
    public static CommonProxy proxy;
    
    public static CreativeTabs tab;

    public static int renderIDPot;
    public static Block pot;
        
    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        proxy.initRenderers();
        
        KCBlocks.init();
        
        CompatabilityRegistry.instance().registerCompat("CoFHAPI|energy", RegisterTime.PREINIT, RFCompat.class);
        CompatabilityRegistry.instance().forceLoad(RFCompat.class);
        
        tab = new CreativeTabsCustom("tabKC.machines", Item.getItemFromBlock(KCBlocks.oven));
        
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandlerKC());
    }
}
