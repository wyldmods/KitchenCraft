package org.wyldmods.kitchencraft.foods;

import net.minecraft.creativetab.CreativeTabs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.common.config.ConfigurationHandler;
import org.wyldmods.kitchencraft.foods.common.item.ItemKCFood;

import tterrag.core.common.util.CreativeTabsCustom;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=Reference.MOD_ID_FOODS, name=Reference.MOD_NAME_FOODS, version=Reference.VERSION)
public class KitchenCraftFoods
{
    @Instance(Reference.MOD_ID_FOODS)
    public static KitchenCraftFoods instance;
        
    public static ItemKCFood veggie, meat;
    
    public static CreativeTabs tab;

    public static final Logger logger = LogManager.getLogger(Reference.MOD_NAME_FOODS);

    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
                
        veggie = new ItemKCFood(false);
        meat = new ItemKCFood(true);
        
        GameRegistry.registerItem(veggie, "veggie", Reference.MOD_ID_FOODS);
        GameRegistry.registerItem(meat, "meat", Reference.MOD_ID_FOODS);
        
        tab = new CreativeTabsCustom("tabKC.foods", veggie);
        
        ConfigurationHandler.postInit();
    }
}
