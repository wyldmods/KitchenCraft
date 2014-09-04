package org.wyldmods.kitchencraft.foods.client.render;

import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderSeedGrass implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        renderer.renderBlockAsItem(Blocks.grass, 0, 1);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        renderer.renderStandardBlock(Blocks.grass, x, y, z);
        
        Tessellator tessellator = Tessellator.instance;
        IIcon seedIcon = block.getIcon(1, 0);
        
        double minU = seedIcon.getMinU(), maxU = seedIcon.getMaxU();
        double minV = seedIcon.getMinV(), maxV = seedIcon.getMaxV();
                
        tessellator.setColorOpaque_F(1, 1, 1);
        
        double pY = y + 1.0001;

        tessellator.addVertexWithUV(x, pY, z + 1, minU, minV);
        tessellator.addVertexWithUV(x + 1, pY, z + 1, maxU, minV);
        tessellator.addVertexWithUV(x + 1, pY, z, maxU, maxV);
        tessellator.addVertexWithUV(x, pY, z, minU, maxV);
        
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return KitchenCraftFoods.renderIDGrass;
    }

}
