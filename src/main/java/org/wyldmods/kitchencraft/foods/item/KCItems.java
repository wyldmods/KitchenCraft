package org.wyldmods.kitchencraft.foods.item;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class KCItems
{
    public static void registerSmelting()
    {
        ItemStack muttonRaw = FoodType.getFood("muttonRaw");
        ItemStack muttonCooked = FoodType.getFood("muttonCooked");

        if (muttonRaw != null && muttonCooked != null)
        {
            GameRegistry.addSmelting(FoodType.getFood("muttonRaw"), FoodType.getFood("muttonCooked"), 1f);
        }
    }
}
