package org.wyldmods.kitchencraft.foods.item;

import static org.wyldmods.kitchencraft.foods.item.FoodType.meats;
import static org.wyldmods.kitchencraft.foods.item.FoodType.veggies;

import java.util.ArrayList;
import java.util.List;

import org.wyldmods.kitchencraft.foods.lib.Reference;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemKCFood extends ItemFood
{
    List<IIcon> icons = new ArrayList<IIcon>();
    
    public ItemKCFood(boolean wolfFood)
    {
        super(0, wolfFood);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "item.kc." + (isWolfsFavoriteMeat() ? meats.get(stack.getItemDamage()).name : veggies.get(stack.getItemDamage()).name);
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        if (isWolfsFavoriteMeat())
        {
            for (FoodType f : meats)
            {
                icons.add(register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + f.texture));
            }
        }
        else
        {
            for (FoodType f : veggies)
            {
                icons.add(register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + f.texture));
            }
        }
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return icons.get(stack.getItemDamage());
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < (isWolfsFavoriteMeat() ? meats.size() : veggies.size()); i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override
    public int func_150905_g(ItemStack stack)
    {
        return isWolfsFavoriteMeat() ? meats.get(stack.getItemDamage()).food : veggies.get(stack.getItemDamage()).food ;
    }
    
    @Override
    public float func_150906_h(ItemStack stack)
    {
        return isWolfsFavoriteMeat() ? meats.get(stack.getItemDamage()).saturation : veggies.get(stack.getItemDamage()).saturation ;
    }
}
