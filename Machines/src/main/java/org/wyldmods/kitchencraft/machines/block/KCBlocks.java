package org.wyldmods.kitchencraft.machines.block;

import net.minecraft.block.Block;

import org.wyldmods.kitchencraft.machines.block.container.BlockOven;
import org.wyldmods.kitchencraft.machines.tile.TileOven;

import cpw.mods.fml.common.registry.GameRegistry;

public class KCBlocks
{
    public static Block oven;
    public static Block pot;

    public static void init()
    {
        oven = new BlockOven();
        GameRegistry.registerBlock(oven, "kc_oven");
        GameRegistry.registerTileEntity(TileOven.class, "kc_oven_te");
        
        pot = new BlockPot();
        GameRegistry.registerBlock(pot, "kc_pot");
    }
}
