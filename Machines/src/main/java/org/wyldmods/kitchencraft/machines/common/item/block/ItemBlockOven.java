package org.wyldmods.kitchencraft.machines.common.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockOven extends ItemBlock
{
    public ItemBlockOven(Block block)
    {
        super(block);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(this, 1, 0));

        if (KitchenCraftMachines.loadRF())
        {
            list.add(new ItemStack(this, 1, 1));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "tile." + (stack.getItemDamage() > 0 ? "kc.oven.rf" : "kc.oven");
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage * 8;
    }
}
