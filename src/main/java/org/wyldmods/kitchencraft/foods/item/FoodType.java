package org.wyldmods.kitchencraft.foods.item;

import java.util.ArrayList;
import java.util.List;

public class FoodType
{
    public static List<FoodType> veggies = new ArrayList<FoodType>();
    public static List<FoodType> meats = new ArrayList<FoodType>();

    public final float saturation;
    public final int food;
    public final String name, texture;
    public final boolean isMeat;
    
    private FoodType(String name, String textureName, int food, float sat, boolean isMeat)
    {
        this.name = name;
        this.texture = textureName;
        this.food = food;
        this.saturation = sat;
        this.isMeat = isMeat;
    }
    
    /**
     * Adds a new food type to the multi-food item
     * @param name - unlocalized name
     * @param texture - texture name (no extension)
     * @param food - drumsticks restored
     * @param saturation - saturation given
     */
    public static void registerFoodType(String name, String texture, int food, float saturation)
    {
        registerFoodType(name, texture, food, saturation, false);
    }

    /**
     * Adds a new food type to the multi-food item
     * @param name - unlocalized name
     * @param texture - texture name (no extension)
     * @param food - drumsticks restored
     * @param saturation - saturation given
     * @param isMeat - whether this is meat (wolf food)
     */
    private static void registerFoodType(String name, String texture, int food, float saturation, boolean isMeat)
    {
        if (isMeat)
        {
            meats.add(new FoodType(name, texture, food, saturation, isMeat));
        }
        else
        {
            veggies.add(new FoodType(name, texture, food, saturation, isMeat));
        }
    }

    public static void registerDefaultFoods()
    {
        registerFoodType("mutton", "mutton", 3, 0.6f, true);
        registerFoodType("kiwi", "kiwi", 10, 2f);
    }
}
