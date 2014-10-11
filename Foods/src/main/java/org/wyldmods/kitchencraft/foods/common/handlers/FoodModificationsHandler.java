package org.wyldmods.kitchencraft.foods.common.handlers;

import squeek.applecore.api.food.FoodEvent;
import squeek.applecore.api.food.FoodValues;
import tterrag.core.common.Handlers.Handler;
import tterrag.core.common.Handlers.Handler.HandlerType;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(types = HandlerType.FORGE)
public class FoodModificationsHandler
{
    @Optional.Method(modid = "AppleCore")
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onFoodEaten(FoodEvent.FoodEaten event)
    {
        if (!event.player.worldObj.isRemote)
        {
            // apply potion effects, etc
        }
    }

    @Optional.Method(modid = "AppleCore")
    @SubscribeEvent(priority = EventPriority.LOW)
    public void getFoodValues(FoodEvent.GetFoodValues event)
    {
        // set or modify food values
        event.foodValues = new FoodValues(event.foodValues.hunger, event.foodValues.saturationModifier);
    }
}
