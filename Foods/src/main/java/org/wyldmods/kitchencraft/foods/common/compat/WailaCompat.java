package org.wyldmods.kitchencraft.foods.common.compat;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import org.wyldmods.kitchencraft.foods.common.block.IKCPlant;


public class WailaCompat implements IWailaDataProvider
{
    public static final WailaCompat INSTANCE = new WailaCompat();
    
    public static void load(IWailaRegistrar registrar)
    {
        registrar.registerHeadProvider(INSTANCE, IKCPlant.class);
        registrar.registerStackProvider(INSTANCE, IKCPlant.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return getFoodStackFrom(accessor);
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        IKCPlant plant = (IKCPlant) accessor.getBlock();
        MovingObjectPosition p = accessor.getPosition();
        ItemStack food = getFoodStackFrom(accessor);
        if (food != accessor.getStack())
        {
            currenttip.set(0, plant.getFood(accessor.getWorld(), p.blockX, p.blockY, p.blockZ).getDisplayName() + " " + plant.getSuffix());
        }
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }
    
    private ItemStack getFoodStackFrom(IWailaDataAccessor accessor)
    {
        IKCPlant block = (IKCPlant) accessor.getBlock();
        MovingObjectPosition pos = accessor.getPosition();
        ItemStack food = block.getFood(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ);
        return food == null ? accessor.getStack() : food;
    }
}
