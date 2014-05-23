package org.wyldmods.kitchencraft.foods;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.config.ConfigurationHandler;
import org.wyldmods.kitchencraft.foods.item.ItemKCFood;

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
    
    public static CreativeTabs tab = new CreativeTabs("tabKC.foods")
    {
        @Override
        public Item getTabIconItem()
        {
            return Items.porkchop;
        }
    };
    
    public static ItemKCFood veggie, meat;
    
    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
                
        veggie = new ItemKCFood(false);
        meat = new ItemKCFood(true);
        
        GameRegistry.registerItem(veggie, "veggie", Reference.MOD_ID_FOODS);
        GameRegistry.registerItem(meat, "meat", Reference.MOD_ID_FOODS);
        
        ConfigurationHandler.postInit();
    }
}
