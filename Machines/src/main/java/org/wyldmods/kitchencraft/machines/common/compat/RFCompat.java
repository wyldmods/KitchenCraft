package org.wyldmods.kitchencraft.machines.common.compat;

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
}
