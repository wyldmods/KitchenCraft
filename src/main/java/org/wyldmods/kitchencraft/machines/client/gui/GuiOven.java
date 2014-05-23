package org.wyldmods.kitchencraft.machines.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.container.ContainerOven;
import org.wyldmods.kitchencraft.machines.tile.TileOven;

public class GuiOven extends GuiContainer
{
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/gui/furnace.png");
    private int x, y;
    private TileOven tile;

    public GuiOven(InventoryPlayer par1InventoryPlayer, TileOven tile)
    {
        super(new ContainerOven(par1InventoryPlayer, tile));
        this.xSize = 192;
        this.ySize = 166;

        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString("Oven", 77, 5, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
    {
        this.mc.renderEngine.bindTexture(texture);

        x = (this.width - this.xSize) / 2;
        y = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(x, y, 0, 0, this.xSize - 16, this.ySize);

        int burn = this.tile.getBurnTimeRemainingScaled(12);
        
        System.out.println(burn);
        
        if (burn != -1)
            this.drawTexturedModalRect(x + 56, y + 36 + 13 - burn, 176, 12 - burn, 14, burn + 2);

        int cook = this.tile.getCookProgressScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, cook + 1, 16);
    }
}
