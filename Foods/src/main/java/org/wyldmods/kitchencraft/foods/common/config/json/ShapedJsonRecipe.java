package org.wyldmods.kitchencraft.foods.common.config.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import scala.actors.threadpool.Arrays;
import cpw.mods.fml.common.registry.GameRegistry;

public class ShapedJsonRecipe
{
    private String[][] input;
    private String output;
    private static final int STARTING_VALUE = Character.valueOf('a');

    public ShapedJsonRecipe(String output, String[][] input)
    {
        this.output = output;
        this.input = input;
    }

    @SuppressWarnings("unchecked")
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
        for (int h = 0; h < height; h++)
        {
            cur = "";
            for (int w = 0; w < width; w++)
            {
                if (recipe.input[h].length > w)
                {
                    cur += (char) (STARTING_VALUE + h + w);
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
            }
        }
        return index;
    }
}
