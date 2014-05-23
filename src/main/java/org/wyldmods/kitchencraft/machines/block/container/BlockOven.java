package org.wyldmods.kitchencraft.machines.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;
import org.wyldmods.kitchencraft.machines.client.gui.GuiHandlerKC.GuiTypes;
import org.wyldmods.kitchencraft.machines.tile.TileOven;

public class BlockOven extends BlockContainerKC
{
    public BlockOven()
    {
        super("kc.oven", Material.rock, soundTypeStone, 1.0f, 0, TileOven.class);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileOven)
        {
            player.openGui(KitchenCraftMachines.instance, GuiTypes.OVEN.ordinal(), world, x, y, z);
            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }
}
