package org.wyldmods.kitchencraft.foods.common.config.json;

import lombok.ToString;
import net.minecraft.block.Block;

@ToString
public class FoodTypeDropped extends FoodType
{
    /* Begin JSON Fields @formatter:off */
    public int      minDropped  = 0;
    public int      maxDropped  = 0;
    public double   dropChance  = 1;
    public String[] animals     = {};
    public String[] blocks      = {};
    /* End JSON Fields @formatter:on */
    
    public BlockEntry getFromString(String s)
    {
        String[] arr = s.split(";");
        Block block = (Block) Block.blockRegistry.getObject(arr[0]);
        int meta = -1;
        if (arr.length > 1)
        {
            meta = Integer.parseInt(arr[1]);
        }
        return new BlockEntry(block, meta);
    }

    public BlockEntry[] getAllEntries()
    {
        BlockEntry[] ret = new BlockEntry[blocks.length];
        for (int i = 0; i < ret.length; i++)
        {
            BlockEntry entry = getFromString(blocks[i]);
            ret[i] = entry;
        }
        return ret;
    }
}
