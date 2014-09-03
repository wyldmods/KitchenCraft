package org.wyldmods.kitchencraft.foods.common.compat;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.wyldmods.kitchencraft.foods.common.block.BlockKCPlant;
import org.wyldmods.kitchencraft.foods.common.item.ItemKCSeed;

import tterrag.core.common.compat.ICompatability;
import crazypants.enderio.machine.farm.TileFarmStation;
import crazypants.enderio.machine.farm.farmers.FarmersCommune;
import crazypants.enderio.machine.farm.farmers.PlantableFarmer;
import crazypants.util.BlockCoord;

public class EnderIOCompat extends PlantableFarmer implements ICompatability
{
    public static void load()
    {
        FarmersCommune.joinCommune(new EnderIOCompat());
    }

    @Override
    public boolean canHarvest(TileFarmStation farm, BlockCoord bc, Block block, int meta)
    {
        return block instanceof BlockKCPlant && farm.getWorldObj().getBlockMetadata(bc.x, bc.y, bc.z) == 7;
    }

    @Override
    public boolean canPlant(ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof ItemKCSeed;
    }

    @Override
    public boolean prepareBlock(TileFarmStation farm, BlockCoord bc, Block block, int meta)
    {
        if (block == null)
        {
            return false;
        }

        ItemStack seedStack = farm.getSeedTypeInSuppliesFor(bc);
        if (seedStack == null)
        {
            farm.setNotification("noSeeds");
            return false;
        }

        if (!canPlant(seedStack))
        {
            return false;
        }

        if (!farm.tillBlock(bc))
        {
            if (!farm.hasHoe())
            {
                farm.setNotification("noHoe");
            }
            return false;
        }
        
        ItemKCSeed seed = (ItemKCSeed) seedStack.getItem();
        if (seed.onItemUse(seedStack, farm.getFakePlayer(), farm.getWorldObj(), bc.x, bc.y - 1, bc.z, 1, 0.5f, 0.5f, 0.5f))
        {
            farm.takeSeedFromSupplies(bc);
        }

        return false;
    }

    // Complications involving IGrowable
    // @Override
    // public IHarvestResult harvestBlock(TileFarmStation farm, BlockCoord bc, Block block, int meta)
    // {
    // World world = farm.getWorldObj();
    // TileEntity te = world.getTileEntity(bc.x, bc.y, bc.z);
    // if (te != null && te instanceof TileKCPlant)
    // {
    // BlockKCPlant plantBlock = (BlockKCPlant) block;
    // int dmg = plantBlock.getFood(world, bc.x, bc.y, bc.z).getItemDamage();
    // HarvestResult result = new HarvestResult();
    // result.getHarvestedBlocks().add(bc);
    // result.getDrops().add(new EntityItem(world, bc.x, bc.y, bc.z, new ItemStack(KCItems.veggie, world.rand.nextInt(4) + 1, dmg)));
    // result.getDrops().add(new EntityItem(world, bc.x, bc.y, bc.z, new ItemStack(KCItems.seed, world.rand.nextInt(3) + 1, dmg)));
    // world.setBlockToAir(bc.x, bc.y, bc.z);
    // return result;
    // }
    // return null;
    // }
}
