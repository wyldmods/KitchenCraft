package org.wyldmods.kitchencraft.machines.client.gui;

import org.wyldmods.kitchencraft.machines.common.container.ContainerOven;
import org.wyldmods.kitchencraft.machines.common.tile.TileKCInventory;
import org.wyldmods.kitchencraft.machines.common.tile.TileOven;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerKC implements IGuiHandler
{
    public enum GuiTypes
    {
        OVEN
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileKCInventory te = getTE(world, x, y, z);
      
        if (te == null)
            return null;

        switch (GuiTypes.values()[ID])
        {
        case OVEN:
            return new ContainerOven(player.inventory, (TileOven) te);
        default:
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileKCInventory te = getTE(world, x, y, z);
        
        if (te == null)
            return null;

        switch (GuiTypes.values()[ID])
        {
        case OVEN:
            return new GuiOven(player.inventory, (TileOven) te);
        default:
            return null;
        }
    }

    private TileKCInventory getTE(World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);

        if (!(te instanceof TileKCInventory))
            return null;

        return (TileKCInventory) te;
    }
}
