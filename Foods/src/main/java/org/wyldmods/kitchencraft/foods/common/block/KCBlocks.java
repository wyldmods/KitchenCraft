package org.wyldmods.kitchencraft.foods.common.block;

import net.minecraft.block.Block;

import org.wyldmods.kitchencraft.foods.common.block.BlockKCLeaves.TileKCLeaves;

import cpw.mods.fml.common.registry.GameRegistry;

public class KCBlocks
{
    public static Block crop;
    public static Block sapling;
    public static Block seedGrass;
    public static Block leaves;
    public static Block fruityLeaves;
    
    public static void preInit()
    {
        crop = new BlockKCPlant();
        sapling = new BlockKCSapling();
        seedGrass = new BlockSeedGrass();
        leaves = new BlockKCLeaves();
        fruityLeaves = new BlockKCLeaves();
        
        GameRegistry.registerBlock(leaves, "leaves");
        GameRegistry.registerBlock(fruityLeaves, "fruityLeaves");
        GameRegistry.registerTileEntity(TileKCLeaves.class, "tileKCLeaves");
    }
}
