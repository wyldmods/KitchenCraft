package org.wyldmods.kitchencraft.foods.common.config.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.wyldmods.kitchencraft.foods.common.item.ItemKCFood;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;

public class FoodType
{
    public static class BlockEntry
    {
        public final Block block;
        public final int metadata;

        public BlockEntry(Block block, int metadata)
        {
            this.block = block;
            this.metadata = metadata;
        }
    }

    public static class PotionEntry
    {
        /* JSON fields @formatter:off */
        public String name   = "null";
        public int          time   = 1200;
        public int          level  = 0;
        public double       chance = 1.0;
        /* end JSON fields @formatter:on */
    }

    public static List<FoodType> veggies = new LinkedList<FoodType>();
    public static List<FoodType> meats = new LinkedList<FoodType>();

    public static Set<String> validAnimals = new HashSet<String>();
    public static Set<BlockEntry> validBlocks = new HashSet<BlockEntry>();

    private static final Random rand = new Random();

    /* JSON fields @formatter:off*/
    public String        name           =  "null";
    public int           food           =  4;
    public float         saturation     =  0.2f;
    public boolean       isMeat         =  false;
    public boolean       isFruit        =  false;
    public int           color          =  0xFFFFFF;
    public boolean       makeSeed       =  true;
    public boolean       isEdible       =  true;
    public boolean       seedRecipe     =  true;
    public PotionEntry[] effects        =  {};
    public boolean       isDrink        =  false;
    public boolean       isAlwaysEdible =  false;
    public String[]      oreDictNames   =  {};
    public String        flavorText     =  null;                                    // will be handled appropriately
    /* end JSON fields @formatter:on*/

    public FoodType()
    {
        ArrayUtils.add(oreDictNames, "food" + StringUtils.capitalize(name));
    }

    public static ItemStack getFood(String name)
    {
        for (int i = 0; i < veggies.size(); i++)
        {
            if (veggies.get(i).name.equals(name))
                return new ItemStack(KCItems.veggie, 1, i);
        }
        for (int i = 0; i < meats.size(); i++)
        {
            if (meats.get(i).name.equals(name))
                return new ItemStack(KCItems.meat, 1, i);
        }
        return null;
    }

    public static FoodType getFoodType(ItemStack stack)
    {
        if (stack.getItem() instanceof ItemKCFood)
        {
            ItemKCFood food = (ItemKCFood) stack.getItem();
            return food.isWolfsFavoriteMeat() ? meats.get(stack.getItemDamage()) : veggies.get(stack.getItemDamage());
        }
        return null;
    }

    public static ItemStack getStackFor(FoodType type)
    {
        Item food = type.isMeat ? KCItems.meat : KCItems.veggie;
        int index = food == KCItems.meat ? meats.indexOf(type) : veggies.indexOf(type);
        return new ItemStack(food, 1, index);
    }

    public static void registerFoodType(FoodType food)
    {
        if (food instanceof FoodTypeDropped)
        {
            validAnimals.addAll(Arrays.asList(((FoodTypeDropped) food).animals));
            validBlocks.addAll(Arrays.asList(((FoodTypeDropped) food).getAllEntries()));
        }

        if (food.isMeat)
            meats.add(food);
        else
            veggies.add(food);
    }

    public static List<ItemStack> getDroppedFoodsFrom(String entityName)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        for (int i = 0; i < veggies.size(); i++)
        {
            FoodType f = veggies.get(i);
            if (f instanceof FoodTypeDropped)
            {
                FoodTypeDropped food = (FoodTypeDropped) f;
                for (String s : food.animals)
                {
                    if (entityName.toLowerCase().contains(s.toLowerCase()))
                    {
                        ret.add(new ItemStack(KCItems.veggie, rand.nextInt(food.maxDropped - food.minDropped + 1) + food.minDropped, i));
                    }
                }
            }
        }
        for (int i = 0; i < meats.size(); i++)
        {
            FoodType f = meats.get(i);
            if (f instanceof FoodTypeDropped)
            {
                FoodTypeDropped food = (FoodTypeDropped) f;
                for (String s : food.animals)
                {
                    if (entityName.toLowerCase().contains(s.toLowerCase()))
                    {
                        ret.add(new ItemStack(KCItems.meat, rand.nextInt(food.maxDropped - food.minDropped + 1) + food.minDropped, i));
                    }
                }
            }
        }

        return ret;
    }

    public static List<ItemStack> getDroppedFoodsFrom(BlockEntry b)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        for (int i = 0; i < veggies.size(); i++)
        {
            FoodType f = veggies.get(i);
            if (f instanceof FoodTypeDropped)
            {
                FoodTypeDropped food = (FoodTypeDropped) f;
                for (String s : food.blocks)
                {
                    BlockEntry entry = food.getFromString(s);
                    if (entry.block == b.block && (entry.metadata == -1 || entry.metadata == b.metadata))
                    {
                        ret.add(new ItemStack(KCItems.veggie, rand.nextInt(food.maxDropped - food.minDropped + 1) + food.minDropped, i));
                    }
                }
            }
        }
        for (int i = 0; i < meats.size(); i++)
        {
            FoodType f = meats.get(i);
            if (f instanceof FoodTypeDropped)
            {
                FoodTypeDropped food = (FoodTypeDropped) f;
                for (String s : food.blocks)
                {
                    BlockEntry entry = food.getFromString(s);
                    if (entry.block == b.block && (entry.metadata == -1 || entry.metadata == b.metadata))
                    {
                        ret.add(new ItemStack(KCItems.meat, rand.nextInt(food.maxDropped - food.minDropped + 1) + food.minDropped, i));
                    }
                }
            }
        }

        return ret;
    }
}
