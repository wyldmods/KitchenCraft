package org.wyldmods.kitchencraft.foods.common.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public interface IKCPlant
{
    public ItemStack getFood(IBlockAccess world, int x, int y, int z);
    
    public void setFood(ItemStack stack, IBlockAccess world, int x, int y, int z);
    
    public String getSuffix();
}
