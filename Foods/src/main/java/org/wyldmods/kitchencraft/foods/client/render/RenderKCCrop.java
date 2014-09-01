package org.wyldmods.kitchencraft.foods.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.block.BlockKCPlant;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderKCCrop implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        renderer.setOverrideBlockTexture(block.getIcon(0, 0));
        renderer.renderStandardBlock(Blocks.stone, 0, 0, 0);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        renderer.renderBlockCrops(block, x, y, z);
        ItemStack food = ((BlockKCPlant)block).getFood(world, x, y, z);
        IIcon icon = food.getItem().getIcon(food, 0);
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1, 1, 1);
        tessellator.addVertexWithUV(x, y + 0.5, z, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(x, y + 0.5, z + 1, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + 1, y + 0.5, z + 1, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + 1, y + 0.5, z, icon.getMaxU(), icon.getMinV());

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return KitchenCraftFoods.renderIDCrop;
    }
}
