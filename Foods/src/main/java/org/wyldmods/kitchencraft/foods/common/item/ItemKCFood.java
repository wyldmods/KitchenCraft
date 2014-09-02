package org.wyldmods.kitchencraft.foods.common.item;

import static org.wyldmods.kitchencraft.foods.KitchenCraftFoods.*;
import static org.wyldmods.kitchencraft.foods.common.config.json.FoodType.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.ConfigurationHandler;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType.PotionEntry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKCFood extends ItemFood
{
    @SideOnly(Side.CLIENT)
    Map<String, IIcon> icons;

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
        icons = new HashMap<String, IIcon>();
        if (isWolfsFavoriteMeat())
        {
            for (FoodType f : meats)
            {
                icons.put(f.name, register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + f.name));
            }
        }
        else
        {
            for (FoodType f : veggies)
            {
                icons.put(f.name, register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + f.name));
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
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < (isWolfsFavoriteMeat() ? meats.size() : veggies.size()); i++)
        {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean inHand)
    {
        FoodType type = getFoodType(stack);
        if (ConfigurationHandler.doFoodTooltips && type.isEdible)
        {
            list.add(EnumChatFormatting.WHITE + lang.localize("tooltip.hunger") + " " + EnumChatFormatting.YELLOW + FoodType.getFoodType(stack).food);
            list.add(EnumChatFormatting.WHITE + lang.localize("tooltip.saturation") + " " + EnumChatFormatting.YELLOW + FoodType.getFoodType(stack).saturation);

            if (type.effects.length > 0)
            {
                list.add(EnumChatFormatting.WHITE + lang.localize("tooltip.whenEaten"));
                for (PotionEntry pot : type.effects)
                {
                    list.add(String.format(EnumChatFormatting.WHITE + "- %s: %s%s %s%s", lang.localize(pot.name, false), EnumChatFormatting.YELLOW, pot.time / 20,
                            EnumChatFormatting.WHITE, lang.localize("tooltip.seconds")));
                }
            }
        }
    }

    @Override
    public int func_150905_g(ItemStack stack)
    {
        return isWolfsFavoriteMeat() ? meats.get(stack.getItemDamage()).food : veggies.get(stack.getItemDamage()).food;
    }

    @Override
    public float func_150906_h(ItemStack stack)
    {
        return isWolfsFavoriteMeat() ? meats.get(stack.getItemDamage()).saturation : veggies.get(stack.getItemDamage()).saturation;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        FoodType type = getFoodType(stack);
        if (type.isEdible && player.canEat(type.isAlwaysEdible))
        {
            player.setItemInUse(stack, stack.getMaxItemUseDuration());
        }
        
        return stack;
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        FoodType type = getFoodType(stack);
        return type.isDrink ? EnumAction.drink : EnumAction.eat;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        PotionEntry[] potions = getFoodType(stack).effects;
        for (PotionEntry p : potions)
        {
            Potion effect = null;
            for (Potion potion : Potion.potionTypes)
            {
                if (potion != null && potion.getName().equals(p.name))
                {
                    effect = potion;
                    break;
                }
            }

            if (effect == null)
                continue;

            PotionEffect active = player.getActivePotionEffect(effect);
            int activeDuration = active == null ? 0 : active.getDuration();
            player.addPotionEffect(new PotionEffect(effect.id, activeDuration + p.time, p.level));
        }

        return super.onEaten(stack, world, player);
    }
}
