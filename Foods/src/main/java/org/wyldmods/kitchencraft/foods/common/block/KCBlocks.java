package org.wyldmods.kitchencraft.foods.common.block;

import net.minecraft.block.Block;

public class KCBlocks
{
    public static Block crop;
    
    public static void init()
    {
        crop = new BlockKCPlant();
    }
}
