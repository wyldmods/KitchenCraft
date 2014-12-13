package org.wyldmods.kitchencraft.foods.common.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;
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

        KitchenCraftFoods.tab.setDisplay(veggie);
    }

    public static void init()
    {
        if (!FoodType.veggies.isEmpty())
        {
            GameRegistry.registerItem(veggie, "veggie", Reference.MOD_ID_FOODS);
            GameRegistry.registerItem(seed, "seed", Reference.MOD_ID_FOODS);
        }
        
        if (!FoodType.meats.isEmpty())
        {
            GameRegistry.registerItem(meat, "meat", Reference.MOD_ID_FOODS);
        }
        
        seed.initSeedRegistrations();
        
        for (int i = 0; i < FoodType.veggies.size(); i++)
        {
            FoodType veg = FoodType.veggies.get(i);
            if (veg.makeSeed && veg.seedRecipe)
            {
                GameRegistry.addShapelessRecipe(new ItemStack(KCItems.seed, 1, i), new ItemStack(KCItems.veggie, 1, i));
            }
        }
        
        for (FoodType type : FoodType.veggies)
        {
            OreDictionary.registerOre("seed" + StringUtils.capitalize(type.name), new ItemStack(seed, 1, FoodType.getFood(type.name).getItemDamage()));
            registerTypeOredict(type);
        }
        
        for (FoodType type : FoodType.meats)
        {
            registerTypeOredict(type);
        }
    }
    
    private static void registerTypeOredict(FoodType type)
    {
        OreDictionary.registerOre("food" + StringUtils.capitalize(type.name), FoodType.getFood(type.name));
        for (String name : type.oreDictNames)
        {
            OreDictionary.registerOre(name, FoodType.getFood(type.name));
        }
    }
}
