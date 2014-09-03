package org.wyldmods.kitchencraft.foods.common.config.json;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class JsonRecipeUtils
{
    public static Object parseStringIntoRecipeItem(String string)
    {
        return parseStringIntoRecipeItem(string, false);
    }
    
    public static Object parseStringIntoRecipeItem(String string, boolean forceItemStack)
    {
        if (OreDictionary.getOres(string).isEmpty() && FoodType.getFood(string) == null)
        {
            ItemStack stack = null;

            String[] info = string.split(";");
            Object temp = null;
            int damage = OreDictionary.WILDCARD_VALUE;
            temp = Item.itemRegistry.getObject(info[0]);
            if (info.length > 1)
            {
                damage = Integer.parseInt(info[1]);
            }

            if (temp instanceof Item)
            {
                stack = new ItemStack((Item) temp, 1, damage);
            }
            else if (temp instanceof Block)
            {
                stack = new ItemStack((Block) temp, 1, damage);
            }
            else if (temp instanceof ItemStack)
            {
                ((ItemStack) temp).setItemDamage(damage);
            }
            else
            {
                throw new IllegalArgumentException(string
                        + " is not a vaild string. Strings should be either an oredict or food name, or in the format objectname;damage (damage is optional)");
            }

            return stack;
        }
        else if (FoodType.getFood(string) != null)
        {
            return FoodType.getFood(string);
        }
        else if (forceItemStack)
        {
            return OreDictionary.getOres(string).get(0);
        }
        else
        {
            return string;
        }
    }
}
