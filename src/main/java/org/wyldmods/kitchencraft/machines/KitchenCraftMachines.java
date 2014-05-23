package org.wyldmods.kitchencraft.machines;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.block.KCBlocks;
import org.wyldmods.kitchencraft.machines.client.gui.GuiHandlerKC;
import org.wyldmods.kitchencraft.machines.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid=Reference.MOD_ID_MACHINES, name=Reference.MOD_NAME_MACHINES, version=Reference.VERSION)
public class KitchenCraftMachines
{
    @Instance(Reference.MOD_ID_MACHINES)
    public static KitchenCraftMachines instance;
    
    @SidedProxy(clientSide=Reference.CLIENT_PROXY_CLASS_MACHINES, serverSide=Reference.SERVER_PROXY_CLASS_MACHINES)
    public static CommonProxy proxy;
    
    public static CreativeTabs tab = new CreativeTabs("tabKC.machines")
    {
        @Override
        public Item getTabIconItem()
        {
            return Blocks.furnace.getItem(null, 0, 0, 0);
        }
    };

    public static int renderIDPot;
    public static Block pot;
        
    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        proxy.initRenderers();
        
        KCBlocks.init();
        
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandlerKC());
    }
}
