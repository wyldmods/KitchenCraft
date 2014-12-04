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

import org.lwjgl.input.Keyboard;
import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.ConfigurationHandler;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType.PotionEntry;

import tterrag.core.common.json.JsonUtils;
import tterrag.core.common.util.TTStringUtils;
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
        if ((isWolfsFavoriteMeat() && meats.size() == 0) || (!isWolfsFavoriteMeat() && veggies.size() == 0))
            return "null";
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
        if (isWolfsFavoriteMeat())
        {
            return meats.isEmpty() ? "null" : meats.get(damage % meats.size()).name;
        }
        else
        {
            return veggies.isEmpty() ? "null" : veggies.get(damage % veggies.size()).name;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return getIconFromDamage(stack.getItemDamage());
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
        String name = getName(par1);
        return name.equals("null") ? null : icons.get(name);
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

        if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("LOL"))
        {
            list.add("�cT�6R�eO�aL�9O�5L�cO�6L\n");
        }
        else if (type.flavorText != null)
        {
            String text = type.flavorText;
            text += "\n ";
            String[] lines = text.split("\n");
            for (String s : lines)
            {
                list.add(EnumChatFormatting.ITALIC + lang.localize(s, false));
            }
        }

        if (ConfigurationHandler.doFoodTooltips && type.isEdible)
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            {
                list.add(EnumChatFormatting.WHITE + lang.localize("tooltip.hunger") + " " + EnumChatFormatting.YELLOW + FoodType.getFoodType(stack).food);
                list.add(EnumChatFormatting.WHITE + lang.localize("tooltip.saturation") + " " + EnumChatFormatting.YELLOW + FoodType.getFoodType(stack).saturation);

                if (type.effects.length > 0)
                {
                    list.add(EnumChatFormatting.WHITE + lang.localize("tooltip.whenEaten"));
                    Potion[] potions = getPotions(type.effects);
                    for (int i = 0 ; i < type.effects.length; i++)
                    {
                        PotionEntry pot = type.effects[i];
                        PotionEffect effect = new PotionEffect(potions[i].id, 0, pot.level);
                        list.add(String.format(EnumChatFormatting.WHITE + "- %s: %s%s %s%s %s(%s%%)", TTStringUtils.getEffectNameWithLevel(effect), EnumChatFormatting.YELLOW,
                                pot.time / 20, EnumChatFormatting.WHITE, lang.localize("tooltip.seconds"), EnumChatFormatting.ITALIC, (int) (pot.chance * 100)));
                    }
                }
            }
            else
            {
                list.add(EnumChatFormatting.WHITE
                        + String.format(lang.localize("tooltip.pressShift"), EnumChatFormatting.AQUA.toString() + EnumChatFormatting.ITALIC + "-", "-"
                                + EnumChatFormatting.WHITE));
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
        return type.isEdible ? type.isDrink ? EnumAction.drink : EnumAction.eat : EnumAction.none;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            FoodType type = getFoodType(stack);
            PotionEntry[] potions = type.effects;
            applyPotions(potions, player, world);
            
            if (type.container != null)
            {
                ItemStack container = (ItemStack) JsonUtils.parseStringIntoRecipeItem(type.container, true);
                if (!player.inventory.addItemStackToInventory(container))
                {
                    player.dropItem(container.getItem(), container.getItemDamage());
                }
            }
        }
        return super.onEaten(stack, world, player);
    }
    
    public static void applyPotions(PotionEntry[] pots, EntityPlayer player, World world)
    {
        Potion[] potions = getPotions(pots);
        
        for (int i = 0; i < potions.length; i++)
        {
            if (potions[i] == null)
                continue;

            if (world.rand.nextDouble() < pots[i].chance)
            {
                PotionEffect active = player.getActivePotionEffect(potions[i]);
                int activeDuration = active == null ? 0 : active.getDuration();
                player.addPotionEffect(new PotionEffect(potions[i].id, activeDuration + pots[i].time, pots[i].level));
            }
        }
    }
    
    private static Potion[] getPotions(PotionEntry[] pots)
    {
        Potion[] potions = new Potion[pots.length];
        
        for (int i = 0; i < pots.length; i++)
        {
            for (Potion potion : Potion.potionTypes)
            {
                if (potion != null && potion.getName().equals(pots[i].name))
                {
                    potions[i] = potion;
                    break;
                }
            }
        }
        
        return potions;
    }
}
