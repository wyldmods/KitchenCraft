package org.wyldmods.kitchencraft.foods.common.config.json;

public class SmeltingRecipeJson
{
    public final String input, output;
    public int outputAmount = 1;
    public float xp = 0;
    
    public SmeltingRecipeJson(String input, String output) // must define input and output
    {
        this.input = input;
        this.output = output;
    }
}
