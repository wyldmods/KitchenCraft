package org.wyldmods.kitchencraft.foods.common.config.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class ShapedJsonRecipe
{
    private final String[][] input;
    private final String output;
    private int outputAmount = 1;
    private static final int STARTING_VALUE = Character.valueOf('a');
    
    public ShapedJsonRecipe(String[][] input, String output) // force input and output to be defined
    {
        this.input = input;
        this.output = output;
    }
    
    public static void addShapedRecipeFromJson(ShapedJsonRecipe recipe)
    {
        int height = recipe.input.length;
        int width = recipe.input[getMaxLengthIndex(recipe.input)].length;

        List<Object> inputs = new ArrayList<Object>();

        for (String[] arr : recipe.input)
        {
            for (String s : arr)
            {
                inputs.add(JsonRecipeUtils.parseStringIntoRecipeItem(s));
            }
        }

        ItemStack output = (ItemStack) JsonRecipeUtils.parseStringIntoRecipeItem(recipe.output, true);

        Object[] recipeArr = new Object[height];
        String cur = null;
        int charIdx = STARTING_VALUE;
        for (int h = 0; h < height; h++)
        {
            cur = "";
            for (int w = 0; w < width; w++)
            {
                if (recipe.input[h].length > w)
                {
                    cur += (char) (charIdx++);
                }
                else
                {
                    cur += " ";
                }
            }
            recipeArr[h] = cur;
        }

        List<Object> toAdd = new ArrayList<Object>(Arrays.asList(recipeArr));
        Iterator<Object> inputIterator = inputs.iterator();

        for (int i = STARTING_VALUE; i <= cur.toCharArray()[cur.length() - 1]; i++)
        {
            toAdd.add((char) i);
            toAdd.add(inputIterator.next());
        }

        output.stackSize = recipe.outputAmount;
        GameRegistry.addRecipe(new ShapedOreRecipe(output, toAdd.toArray()));
    }

    private static int getMaxLengthIndex(Object[][] arr)
    {
        int index = 0;
        int maxSize = 0;
        for (int i = 0; i < arr.length; i++)
        {
            Object[] arr2 = arr[i];
            if (arr2.length > maxSize)
            {
                index = i;
                maxSize = arr2.length;
            }
        }
        return index;
    }
}
