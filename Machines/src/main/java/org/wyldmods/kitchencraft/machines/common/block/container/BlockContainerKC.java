package org.wyldmods.kitchencraft.machines.common.block.container;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.machines.common.block.BlockKC;
import org.wyldmods.kitchencraft.machines.common.tile.TileKCInventory;

public class BlockContainerKC extends BlockKC implements ITileEntityProvider
{
    private Class<? extends TileEntity> teClass;

    protected BlockContainerKC(String unlocName, Material mat, SoundType type, float hardness, int renderID, Class<? extends TileEntity> te)
    {
        super(unlocName, mat, type, hardness, renderID);

        this.teClass = te;
    }

    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return createNewTileEntity(world, metadata);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        try
        {
            return teClass.newInstance();
        }
        catch (Throwable cat)
        {
            cat.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile != null && tile instanceof TileKCInventory && tile != null && !world.isRemote)
        {
            TileKCInventory ee2Tile = (TileKCInventory) tile;
            for (int i = 0; i < ee2Tile.getSizeInventory(); i++)
            {
                ItemStack stack = ee2Tile.getStackInSlotOnClosing(i);

                if (stack != null)
                {
                    float spawnX = x + world.rand.nextFloat() - 0.5f;
                    float spawnY = y + world.rand.nextFloat() - 0.5f;
                    float spawnZ = z + world.rand.nextFloat() - 0.5f;

                    EntityItem entity = new EntityItem(world, spawnX, spawnY, spawnZ, stack);

                    float mult = .05F;

                    entity.motionX = (-.5F + world.rand.nextFloat()) * mult;
                    entity.motionY = (4F + world.rand.nextFloat()) * mult;
                    entity.motionZ = (-.5F + world.rand.nextFloat()) * mult;

                    world.spawnEntityInWorld(entity);
                }
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }
}
