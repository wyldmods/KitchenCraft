package org.wyldmods.kitchencraft.foods.item;

import static org.wyldmods.kitchencraft.foods.item.FoodType.*;
import cpw.mods.fml.common.registry.GameRegistry;

public class KCItems
{
    public static void registerDefaultFoods()
    {
        registerFoodType("muttonRaw", 2, 0.4f, true);
        registerFoodType("muttonCooked", 8, 2f, true);

        registerFoodType("kiwi", 4, 0.5f);
    }
    
    public static void registerSmelting()
    {
        GameRegistry.addSmelting(FoodType.getFood("muttonRaw"), FoodType.getFood("muttonCooked"), 1f);
    }
}
