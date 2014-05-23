package org.wyldmods.kitchencraft.machines.block.container;

import org.wyldmods.kitchencraft.machines.block.BlockKC;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
}
