package org.wyldmods.kitchencraft.foods.common.config.json;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.wyldmods.kitchencraft.foods.common.config.ConfigurationHandler;

import cpw.mods.fml.common.registry.GameRegistry;

public class ShapelessJsonRecipe
{
    public String[] input;
    public String output;
    public int outputAmount = 1;

    public static void addShapelessRecipeFromJson(ShapelessJsonRecipe recipe)
    {
        if (recipe.input == null || recipe.output == null)
        {
            throw new InvalidShapelessRecipeException((recipe.input == null ? "Input was null" : "Output was null") + ". You must define this value.");
        }
        
        List<Object> inputs = new ArrayList<Object>();
        for (String input : recipe.input)
        {
            inputs.add(ConfigurationHandler.parseInputString(input));
        }
        
        ItemStack output = (ItemStack) ConfigurationHandler.parseInputString(recipe.output, true);
        
        output.stackSize = recipe.outputAmount;
        GameRegistry.addRecipe(new ShapelessOreRecipe(output, inputs.toArray()));
    }
    
    @SuppressWarnings("serial")
    private static class InvalidShapelessRecipeException extends RuntimeException
    {
        public InvalidShapelessRecipeException(String text)
        {
            super(text);
        }
    }
}
