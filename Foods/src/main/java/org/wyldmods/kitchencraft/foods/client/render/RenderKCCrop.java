package org.wyldmods.kitchencraft.foods.client.render;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.block.BlockKCPlant;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderKCCrop implements ISimpleBlockRenderingHandler
{
    private static final Random rand = new Random();
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        renderer.setOverrideBlockTexture(block.getIcon(0, 0));
        renderer.renderStandardBlock(Blocks.stone, 0, 0, 0);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        renderer.setOverrideBlockTexture(block.getIcon(world, x, y, z, 0));
        renderer.renderBlockCrops(block, x, y, z);
        renderer.clearOverrideBlockTexture();
        int meta = world.getBlockMetadata(x, y, z);
        ItemStack stack = ((BlockKCPlant)block).getFood(world, x, y, z);

        if (meta >= 7 && !FoodType.getFoodType(stack).hasCropTexture)
        {
            IIcon icon = ((BlockKCPlant) block).getFoodIcon(world, x, y, z);

            Tessellator tessellator = Tessellator.instance;

            rand.setSeed(x + y + z);
            
            double size = 0.4;
            double xBase = x + 0.5;
            double yBase = getRandYOffset(y); // do it once because for some reason first call always returns the same value
            double zBase = z + 0.5;

            double offset = 0.251;

            double minU = icon.getMinU();
            double maxU = icon.getMaxU();
            double minV = icon.getMinV();
            double maxV = icon.getMaxV();

            tessellator.setColorOpaque_F(1, 1, 1);

            // side 1
            zBase -= offset;
            xBase -= size / 2;
            yBase = getRandYOffset(y);
            tessellator.addVertexWithUV(xBase, yBase + size, zBase, maxU, minV);
            tessellator.addVertexWithUV(xBase + size, yBase + size, zBase, minU, minV);
            tessellator.addVertexWithUV(xBase + size, yBase, zBase, minU, maxV);
            tessellator.addVertexWithUV(xBase, yBase, zBase, maxU, maxV);

            // side 2
            zBase += offset * 2;
            yBase = getRandYOffset(y);
            tessellator.addVertexWithUV(xBase, yBase, zBase, minU, maxV);
            tessellator.addVertexWithUV(xBase + size, yBase, zBase, maxU, maxV);
            tessellator.addVertexWithUV(xBase + size, yBase + size, zBase, maxU, minV);
            tessellator.addVertexWithUV(xBase, yBase + size, zBase, minU, minV);

            // reset values
            xBase = x + 0.5;
            zBase = z + 0.5;

            // side 3
            xBase -= offset;
            zBase -= size / 2;
            yBase = getRandYOffset(y);
            tessellator.addVertexWithUV(xBase, yBase, zBase, minU, maxV);
            tessellator.addVertexWithUV(xBase, yBase, zBase + size, maxU, maxV);
            tessellator.addVertexWithUV(xBase, yBase + size, zBase + size, maxU, minV);
            tessellator.addVertexWithUV(xBase, yBase + size, zBase, minU, minV);

            // side 4
            xBase += offset * 2;
            yBase = getRandYOffset(y);
            tessellator.addVertexWithUV(xBase, yBase, zBase, minU, maxV);
            tessellator.addVertexWithUV(xBase, yBase + size, zBase, minU, minV);
            tessellator.addVertexWithUV(xBase, yBase + size, zBase + size, maxU, minV);
            tessellator.addVertexWithUV(xBase, yBase, zBase + size, maxU, maxV);
        }
        return true;
    }

    private double getRandYOffset(int y)
    {
        return MathHelper.clamp_double(y + 0.2 + ((rand.nextDouble() * 0.3) - 0.15), y, y + 0.5);
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
