package org.wyldmods.kitchencraft.foods.common.item;

import net.minecraft.item.ItemStack;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;

import cpw.mods.fml.common.registry.GameRegistry;

public class KCItems
{
    public static ItemKCFood veggie, meat;
    public static ItemKCSeed seed;

    public static void preInit()
    {
        veggie = new ItemKCFood(false);
        meat = new ItemKCFood(true);
        seed = new ItemKCSeed();

        GameRegistry.registerItem(veggie, "veggie", Reference.MOD_ID_FOODS);
        GameRegistry.registerItem(meat, "meat", Reference.MOD_ID_FOODS);
        GameRegistry.registerItem(seed, "seed", Reference.MOD_ID_FOODS);
        
        KitchenCraftFoods.tab.setDisplay(veggie);
    }

    public static void init()
    {
        seed.initSeedRegistrations();
        
        for (int i = 0; i < FoodType.veggies.size(); i++)
        {
            FoodType veg = FoodType.veggies.get(i);
            if (veg.makeSeed)
            {
                GameRegistry.addShapelessRecipe(new ItemStack(KCItems.seed, 1, i), new ItemStack(KCItems.veggie, 1, i));
            }
        }
    }
}
