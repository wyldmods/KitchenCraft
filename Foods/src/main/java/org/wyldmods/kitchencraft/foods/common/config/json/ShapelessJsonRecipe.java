package org.wyldmods.kitchencraft.foods.common.config.json;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class ShapelessJsonRecipe
{
    public final String[] input;
    public final String output;
    public int outputAmount = 1;

    public ShapelessJsonRecipe(String output, String... input)
    {
        this.input = input;
        this.output = output;
    }

    public static void addShapelessRecipeFromJson(ShapelessJsonRecipe recipe)
    {
        List<Object> inputs = new ArrayList<Object>();
        for (String input : recipe.input)
        {
            inputs.add(JsonRecipeUtils.parseStringIntoRecipeItem(input));
        }
        
        ItemStack output = (ItemStack) JsonRecipeUtils.parseStringIntoRecipeItem(recipe.output, true);
        
        output.stackSize = recipe.outputAmount;
        GameRegistry.addRecipe(new ShapelessOreRecipe(output, inputs.toArray()));
    }
}
