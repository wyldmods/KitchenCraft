package org.wyldmods.kitchencraft.foods.item;

import java.util.HashSet;
import java.util.Set;

public class FoodType
{
    public static Set<FoodType> foods = new HashSet<FoodType>();
    
    public final float food, sat;
    public final String name, texture;
    
    private FoodType(String name, String textureName, float food, float sat)
    {
        this.name = name;
        this.texture = textureName;
        this.food = food;
        this.sat = sat;
    }
    
    /**
     * Adds a new food type to the multi-food item
     * @param name - unlocalized name
     * @param texture - texture name (no extension)
     * @param food - drumsticks restored
     * @param saturation - saturation given
     */
    public static void registerFoodType(String name, String texture, float food, float saturation)
    {
        foods.add(new FoodType(name, texture, food, saturation));
    }
}
