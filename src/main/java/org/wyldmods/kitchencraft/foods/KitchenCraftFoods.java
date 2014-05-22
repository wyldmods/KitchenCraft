package org.wyldmods.kitchencraft.foods;

import org.wyldmods.kitchencraft.foods.item.FoodType;
import org.wyldmods.kitchencraft.foods.item.ItemKCFood;
import org.wyldmods.kitchencraft.lib.Reference;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.VERSION)
public class KitchenCraftFoods
{
    @Instance
    public static KitchenCraftFoods instance;
    
    public static ItemKCFood veggie, meat;
    
    @EventHandler
    public static void preinit(FMLPreInitializationEvent event)
    {
        FoodType.registerDefaultFoods();
        
        veggie = new ItemKCFood(false);
        meat = new ItemKCFood(true);
        
        GameRegistry.registerItem(veggie, "veggie", Reference.MOD_ID);
        GameRegistry.registerItem(meat, "meat", Reference.MOD_ID);
    }
}