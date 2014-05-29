package org.wyldmods.kitchencraft.foods.item;

import org.wyldmods.kitchencraft.foods.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.config.json.SmeltingRecipeJson;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class KCItems
{
    public static void registerSmeltingRecipe(SmeltingRecipeJson recipe)
    {
        ItemStack input = FoodType.getFood(recipe.input);
        ItemStack output = FoodType.getFood(recipe.output);
        
        System.out.println("adding recipe for " + recipe.input + " to " + recipe.output);
        System.out.println(input + "   " + output);
        
        if (input != null && output != null)
        {
            GameRegistry.addSmelting(input, output, recipe.xp);
        }
    }
}
