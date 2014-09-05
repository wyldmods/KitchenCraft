package org.wyldmods.kitchencraft.foods.common.compat;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.wyldmods.kitchencraft.foods.common.block.KCBlocks;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;

import tterrag.core.common.compat.ICompatability;
import codechicken.nei.api.API;

public class NEICompat implements ICompatability
{
    public static void load()
    {
        if (FoodType.veggies.isEmpty())
        {
            API.hideItem(new ItemStack(KCItems.veggie, 1, OreDictionary.WILDCARD_VALUE));
        }

        if (FoodType.meats.isEmpty())
        {
            API.hideItem(new ItemStack(KCItems.meat, 1, OreDictionary.WILDCARD_VALUE));
        }

        boolean foundSeed = false;
        for (FoodType type : FoodType.veggies)
        {
            foundSeed |= type.makeSeed;
        }

        if (!foundSeed)
        {
            API.hideItem(new ItemStack(KCItems.seed, 1, OreDictionary.WILDCARD_VALUE));
        }
        
        API.hideItem(new ItemStack(KCBlocks.fruityLeaves));
        API.hideItem(new ItemStack(KCBlocks.seedGrass));
    }
}
