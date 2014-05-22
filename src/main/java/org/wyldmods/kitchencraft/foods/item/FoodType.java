package org.wyldmods.kitchencraft.foods.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;

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
     * @param name - unlocalized name (also used for texture)
     * @param food - drumsticks restored
     * @param saturation - saturation given
     */
    public static void registerFoodType(String name, int food, float saturation)
    {
        registerFoodType(name, name, food, saturation, false);
    }
    
    /**
     * Adds a new food type to the multi-food item
     * @param name - unlocalized name (also used for texture)
     * @param food - drumsticks restored
     * @param saturation - saturation given
     */
    public static void registerFoodType(String name, int food, float saturation, boolean isMeat)
    {
        registerFoodType(name, name, food, saturation, isMeat);
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
    
    public static ItemStack getFood(String name)
    {
        for (int i = 0; i < veggies.size(); i++)
        {
            if (veggies.get(i).name.equals(name))
                return new ItemStack(KitchenCraftFoods.veggie, 1, i);
        }
        for (int i = 0; i < meats.size(); i++)
        {
            if (meats.get(i).name.equals(name))
                return new ItemStack(KitchenCraftFoods.meat, 1, i);
        }
        return null;
    }
}
