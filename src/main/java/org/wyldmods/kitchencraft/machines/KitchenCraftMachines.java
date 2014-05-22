package org.wyldmods.kitchencraft.machines;

import org.wyldmods.kitchencraft.foods.lib.Reference;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.VERSION)
public class KitchenCraftMachines
{
    @Instance
    public static KitchenCraftMachines instance;
        
    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        
    }
}
