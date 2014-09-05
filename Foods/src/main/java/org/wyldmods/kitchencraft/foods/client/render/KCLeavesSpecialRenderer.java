package org.wyldmods.kitchencraft.foods.client.render;

import java.util.Random;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.wyldmods.kitchencraft.foods.common.block.BlockKCLeaves.TileKCLeaves;

import tterrag.core.client.util.RenderingUtils;

public class KCLeavesSpecialRenderer extends TileEntitySpecialRenderer
{
    private EntityItem item;
    private static Random rand = new Random();

    private void renderLeavesAt(TileKCLeaves tile, double x, double y, double z, float partialTickTime)
    {
        rand.setSeed(tile.xCoord + tile.yCoord + tile.zCoord);
        item = new EntityItem(tile.getWorldObj(), x, y, z, tile.getFood());
        
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        for (int i = 0; i < tile.amnt; i++)
        {
            x = rand.nextDouble() * 0.6 + 0.2;
            y = rand.nextDouble() * 0.5;
            z = rand.nextDouble() * 0.6 + 0.2;

            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            RenderingUtils.render3DItem(item, partialTickTime, false);
            GL11.glPopMatrix();
        }
        
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTickTime)
    {
        if (tile instanceof TileKCLeaves)
        {
            renderLeavesAt((TileKCLeaves) tile, x, y, z, partialTickTime);
        }
    }
}
