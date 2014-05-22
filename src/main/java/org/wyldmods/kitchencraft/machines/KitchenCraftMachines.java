package org.wyldmods.kitchencraft.machines;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import org.wyldmods.kitchencraft.machines.block.BlockPot;
import org.wyldmods.kitchencraft.machines.lib.Reference;
import org.wyldmods.kitchencraft.machines.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.VERSION)
public class KitchenCraftMachines
{
    @Instance(Reference.MOD_ID)
    public static KitchenCraftMachines instance;
    
    @SidedProxy(clientSide=Reference.CLIENT_PROXY_CLASS, serverSide=Reference.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;
    
    public static CreativeTabs tab = new CreativeTabs(Reference.MOD_NAME)
    {
        @Override
        public Item getTabIconItem()
        {
            return Items.porkchop;
        }
    };

    public static int renderIDPot;
    public static Block pot;
        
    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        proxy.initRenderers();
        
        pot = new BlockPot();
        GameRegistry.registerBlock(pot, "kc_pot");
    }
}
