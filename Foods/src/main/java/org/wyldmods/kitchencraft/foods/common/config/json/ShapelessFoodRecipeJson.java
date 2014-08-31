package org.wyldmods.kitchencraft.foods.common.config.json;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

public class ShapelessFoodRecipeJson
{
    public final String[] input;
    public final String output;
    
    public ShapelessFoodRecipeJson(String output, String... input)
    {
        this.input = input;
        this.output = output;
    }
    
    public static void addShapelessRecipeFromJson(ShapelessFoodRecipeJson recipe)
    {
        List<ItemStack> inputs = new ArrayList<ItemStack>();
        ItemStack output = FoodType.getFood(recipe.output);

        for (String s : recipe.input)
        {
            ItemStack i = FoodType.getFood(s);
            if (output == null || i == null) return;
            else inputs.add(i);
        }
        
        GameRegistry.addShapelessRecipe(output, inputs.toArray());
    }
}
