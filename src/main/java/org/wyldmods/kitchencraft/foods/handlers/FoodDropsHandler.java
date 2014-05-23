package org.wyldmods.kitchencraft.foods.handlers;

import java.util.List;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

import org.wyldmods.kitchencraft.foods.config.json.FoodType;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FoodDropsHandler
{
    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event)
    {
        if (event.entityLiving instanceof EntityLiving)
        {
            EntityLiving entity = (EntityLiving) event.entityLiving;
            String entityName = (String) EntityList.classToStringMapping.get(entity.getClass());
            for (String s : FoodType.validAnimals)
            {
                if (entityName.toLowerCase().contains(s.toLowerCase()))
                {
                    List<ItemStack> toAdd = FoodType.getDroppedFoodsFrom(entityName);
                    for (ItemStack i : toAdd)
                    {
                        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, i));
                    }
                }
            }
        }
    }
}
