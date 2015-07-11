package org.wyldmods.kitchencraft.machines.common.compat;

import net.minecraftforge.common.util.ForgeDirection;

import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;
import org.wyldmods.kitchencraft.machines.common.tile.TileOven;
import org.wyldmods.kitchencraft.machines.common.tile.TileOvenRF;

import cpw.mods.fml.common.registry.GameRegistry;

public class RFCompat
{
    public static void load()
    {
        GameRegistry.registerTileEntity(TileOvenRF.class, "kc.oven.rf");
    }

    public static TileOven getRFOven()
    {
        return new TileOvenRF();
    }

    public static boolean isRFOven(TileOven tile)
    {
        return KitchenCraftMachines.loadRF() && isRFOven_internal(tile);
    }

    private static boolean isRFOven_internal(TileOven tile)
    {
        return tile instanceof TileOvenRF;
    }

    public static int getEnergyStored(TileOven tile, ForgeDirection dir)
    {
        if (KitchenCraftMachines.loadRF())
        {
            return getEnergyStored_internal(tile, dir);
        }
        return 0;
    }

    private static int getEnergyStored_internal(TileOven tile, ForgeDirection dir)
    {
        if (isRFOven_internal(tile))
        {
            return ((TileOvenRF) tile).getEnergyStored(dir);
        }
        return 0;
    }

    public static void setEnergyStored(TileOven tile, int energy)
    {
        if (KitchenCraftMachines.loadRF())
        {
            setEnergyStored_internal(tile, energy);
        }
    }

    private static void setEnergyStored_internal(TileOven tile, int energy)
    {
        if (isRFOven_internal(tile))
        {
            ((TileOvenRF) tile).setEnergyStored(energy);
        }
    }
}
