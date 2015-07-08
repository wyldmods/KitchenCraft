package org.wyldmods.kitchencraft.foods.common.handlers;

import java.util.List;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.input.Keyboard;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodModification;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType.PotionEntry;
import org.wyldmods.kitchencraft.foods.common.item.ItemKCFood;

import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;

import com.enderio.core.common.Handlers.Handler;
import com.enderio.core.common.Handlers.Handler.HandlerType;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static org.wyldmods.kitchencraft.foods.KitchenCraftFoods.lang;

@Handler(HandlerType.FORGE)
public class FoodModificationsHandler
{
    @Optional.Method(modid = "AppleCore")
    @SubscribeEvent(priority = EventPriority.LOW)
    public void getFoodValues(FoodEvent.GetFoodValues event)
    {
        ItemStack stack = event.food;
        for (FoodModification mod : FoodModification.getValues())
        {
            ItemStack toCheck = mod.getStack();
            if (matches(stack, toCheck))
            {
                int food =  mod.food < 0       ? event.foodValues.hunger              : mod.food;
                float sat = mod.saturation < 0 ? event.foodValues.saturationModifier  : mod.saturation;
                
                event.foodValues = new FoodValues(food, sat);
            }
        }
    }

    @Optional.Method(modid = "AppleCore")
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onFoodEaten(FoodEvent.FoodEaten event)
    {
        if (!event.player.worldObj.isRemote)
        {
            for (FoodModification mod : FoodModification.getValues())
            {
                ItemStack toCheck = mod.getStack();
                if (matches(event.food, toCheck))
                {
                    ItemKCFood.applyPotions(mod.effects, event.player, event.player.worldObj);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Optional.Method(modid = "AppleCore")
    @SubscribeEvent
    public void onFoodTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.itemStack;
        List<String> list = event.toolTip;
        if (stack.getItem() instanceof ItemFood)
        {
            for (FoodModification mod : FoodModification.getValues())
            {
                ItemStack toCheck = mod.getStack();
                if (matches(stack, toCheck))
                {
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
                    {
                        list.add(EnumChatFormatting.WHITE + lang.localize("tooltip.hunger") + " " + EnumChatFormatting.YELLOW + (mod.food < 0 ? lang.localize("tooltip.unchanged") : mod.food));
                        list.add(EnumChatFormatting.WHITE + lang.localize("tooltip.saturation") + " " + EnumChatFormatting.YELLOW + (mod.saturation < 0 ? lang.localize("tooltip.unchanged") : mod.saturation));

                        if (mod.effects.length > 0)
                        {
                            list.add(EnumChatFormatting.WHITE + lang.localize("tooltip.whenEaten"));
                            for (PotionEntry pot : mod.effects)
                            {
                                list.add(String.format(EnumChatFormatting.WHITE + "- %s: %s%s %s%s %s(%s%%)", lang.localize(pot.name, false), EnumChatFormatting.YELLOW,
                                        pot.time / 20, EnumChatFormatting.WHITE, lang.localize("tooltip.seconds"), EnumChatFormatting.ITALIC, (int) (pot.chance * 100)));
                            }
                        }
                    }
                    else
                    {
                        list.add(EnumChatFormatting.WHITE
                                + String.format(lang.localize("tooltip.mod_pressShift"), EnumChatFormatting.AQUA.toString() + EnumChatFormatting.ITALIC + "-", "-"
                                        + EnumChatFormatting.WHITE));
                    }
                    break;
                }
            }
        }
    }

    private boolean matches(ItemStack stack, ItemStack toCheck)
    {
        return stack.getItem() == toCheck.getItem() && (stack.getItemDamage() == toCheck.getItemDamage() || toCheck.getItemDamage() == OreDictionary.WILDCARD_VALUE);
    }
}
