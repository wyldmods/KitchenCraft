package org.wyldmods.kitchencraft.foods.common.config.json;

public class FoodTypeDropped extends FoodType
{
    public final int minDropped, maxDropped;
    public final String[] animals;
    
    protected FoodTypeDropped(String name, int food, float sat, boolean isMeat, int minDropped, int maxDropped, String... animals)
    {
        super(name, food, sat, isMeat);
        
        this.minDropped = minDropped;
        this.maxDropped = maxDropped;
        this.animals = animals;
    }
}
