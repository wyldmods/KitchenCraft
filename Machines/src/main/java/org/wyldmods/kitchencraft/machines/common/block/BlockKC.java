package org.wyldmods.kitchencraft.machines.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;

public class BlockKC extends Block
{
    private int renderID;
    protected String unlocName;

    protected BlockKC(String unlocName, Material mat, SoundType type, float hardness, int renderID)
    {
        super(mat);
        setStepSound(type);
        setHardness(hardness);
        setBlockName(unlocName);
        setCreativeTab(KitchenCraftMachines.tab);
        this.renderID = renderID;
        this.unlocName = unlocName;
    }
    
    public boolean hasCustomModel()
    {
        return true;
    }
    
    @Override
    public int getRenderType()
    {
        return renderID;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return !hasCustomModel();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return !hasCustomModel();
    }
    
    @Override
    public boolean isNormalCube()
    {
        return !hasCustomModel();
    }
}
