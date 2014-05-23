package org.wyldmods.kitchencraft.foods.item;

import static org.wyldmods.kitchencraft.foods.config.json.FoodType.meats;
import static org.wyldmods.kitchencraft.foods.config.json.FoodType.veggies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.config.json.FoodType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKCFood extends ItemFood
{
    @SideOnly(Side.CLIENT)
    Map<String, IIcon> icons = new HashMap<String, IIcon>();
    
    public ItemKCFood(boolean wolfFood)
    {
        super(0, wolfFood);
        setCreativeTab(KitchenCraftFoods.tab);
        setHasSubtypes(true);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "item.kc." + (isWolfsFavoriteMeat() ? meats.get(stack.getItemDamage()).name : veggies.get(stack.getItemDamage()).name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {        
        if (isWolfsFavoriteMeat())
        {
            for (FoodType f : meats)
            {
                icons.put(f.name, register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + f.texture));
            }
        }
        else
        {
            for (FoodType f : veggies)
            {
                icons.put(f.name, register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + f.texture));
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    private String getName(ItemStack stack)
    {
        return getName(stack.getItemDamage());
    }
    
    @SideOnly(Side.CLIENT)
    private String getName(int damage)
    {
        return isWolfsFavoriteMeat() ? meats.get(damage).name : veggies.get(damage).name;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return icons.get(getName(stack));
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        return getIcon(stack, renderPass);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return icons.get(getName(par1));
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
