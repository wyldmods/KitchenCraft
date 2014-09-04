package org.wyldmods.kitchencraft.foods.common.block;

import net.minecraft.block.Block;

public class KCBlocks
{
    public static Block crop;
    public static Block sapling;
    public static Block wood;
    public static Block leaves;
    public static Block seedGrass;
    
    public static void preInit()
    {
        crop = new BlockKCPlant();
        sapling = new BlockKCSapling();
        seedGrass = new BlockSeedGrass();
    }
}
