package org.wyldmods.kitchencraft.foods.common.item;

import net.minecraft.item.ItemStack;

import org.wyldmods.kitchencraft.foods.common.config.json.JsonRecipeUtils;
import org.wyldmods.kitchencraft.foods.common.config.json.SmeltingRecipeJson;

import cpw.mods.fml.common.registry.GameRegistry;

public class KCItems
{
    public static void registerSmeltingRecipe(SmeltingRecipeJson recipe)
    {
        ItemStack input = (ItemStack) JsonRecipeUtils.parseStringIntoRecipeItem(recipe.input, true);
        ItemStack output = (ItemStack) JsonRecipeUtils.parseStringIntoRecipeItem(recipe.output, true);
        
        if (input != null && output != null)
        {
            GameRegistry.addSmelting(input, output, recipe.xp);
        }
    }
}
