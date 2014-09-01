package org.wyldmods.kitchencraft.foods.common.compat;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.block.BlockKCPlant;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;


public class WailaCompat implements IWailaDataProvider
{
    public static final WailaCompat INSTANCE = new WailaCompat();
    
    public static void load(IWailaRegistrar registrar)
    {
        registrar.registerHeadProvider(INSTANCE, BlockKCPlant.class);
        registrar.registerStackProvider(INSTANCE, BlockKCPlant.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return getFoodStackFrom(accessor);
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        currenttip.set(0, currenttip.get(0) + " " + KitchenCraftFoods.lang.localize("tooltip.plant"));
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
        BlockKCPlant block = (BlockKCPlant) accessor.getBlock();
        MovingObjectPosition pos = accessor.getPosition();
        return block.getFood(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ);
    }
}
