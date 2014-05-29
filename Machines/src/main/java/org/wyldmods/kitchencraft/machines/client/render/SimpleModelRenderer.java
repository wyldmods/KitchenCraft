package org.wyldmods.kitchencraft.machines.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
 
public class SimpleModelRenderer implements ISimpleBlockRenderingHandler
{
    private final int renderId;
    private final Tessellator tes = Tessellator.instance;
    private final WavefrontObject model;
    
    public SimpleModelRenderer(WavefrontObject model)
    {
        renderId = RenderingRegistry.getNextAvailableRenderId();
        this.model = model;
    }
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        RenderHelper.disableStandardItemLighting();
        tes.startDrawingQuads();
        tes.setColorOpaque_F(1, 1, 1);
        renderWithIcon(model, block.getIcon(0, metadata), tes);
        tes.draw();
        RenderHelper.enableStandardItemLighting();
    }
 
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tes.setColorOpaque_F(1, 1, 1);
        tes.addTranslation(x + .5F, y + .5F, z + .5F);
        renderWithIcon(model, block.getIcon(0, world.getBlockMetadata(x, y, z)), tes);
        tes.addTranslation(-x - .5F, -y - .5F, -z - .5F);
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
        return renderId;
    }
    
    public static void renderWithIcon(WavefrontObject model, IIcon icon, Tessellator tes)
    {
        for(GroupObject go : model.groupObjects)
        {
            for(Face f : go.faces) {
                Vertex n = f.faceNormal;
                tes.setNormal(n.x, n.y, n.z);
                for(int i = 0; i < f.vertices.length; i++) 
                {
                    Vertex v = f.vertices[i];
                    TextureCoordinate t = f.textureCoordinates[i];
                    tes.addVertexWithUV(v.x, v.y, v.z,
                        icon.getInterpolatedU(t.u * 16),
                        icon.getInterpolatedV(t.v * 16));
                }
            }
        }
    }
}
