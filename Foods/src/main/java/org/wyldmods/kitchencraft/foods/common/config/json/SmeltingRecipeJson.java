package org.wyldmods.kitchencraft.foods.common.config.json;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class SmeltingRecipeJson
{
    public String input, output;
    public int outputAmount = 1;
    public float xp = 0;

    public static void registerSmeltingRecipe(SmeltingRecipeJson recipe)
    {
        if (recipe.input == null || recipe.output == null)
        {
            throw new InvalidSmeltingRecipeException((recipe.input == null ? "Input was null" : "Output was null") + ". You must define this value.");
        }
        
        ItemStack input = (ItemStack) JsonRecipeUtils.parseStringIntoRecipeItem(recipe.input, true);
        ItemStack output = (ItemStack) JsonRecipeUtils.parseStringIntoRecipeItem(recipe.output, true);

        output.stackSize = recipe.outputAmount;
        
        if (input != null && output != null)
        {
            GameRegistry.addSmelting(input, output, recipe.xp);
        }
    }
    
    @SuppressWarnings("serial")
    private static class InvalidSmeltingRecipeException extends RuntimeException
    {
        public InvalidSmeltingRecipeException(String text)
        {
            super(text);
        }
    }
}
