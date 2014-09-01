package org.wyldmods.kitchencraft.machines.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.common.container.ContainerOven;
import org.wyldmods.kitchencraft.machines.common.tile.TileOven;
import org.wyldmods.kitchencraft.machines.common.tile.TileOvenRF;

public class GuiOven extends GuiContainer
{
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/gui/oven_gui.png");
    private static final ResourceLocation textureRF = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/gui/oven_gui_rf.png");
    private int x, y;
    private TileOven tile;

    private boolean rf;

    public GuiOven(InventoryPlayer par1InventoryPlayer, TileOven tile)
    {
        super(new ContainerOven(par1InventoryPlayer, tile));
        this.xSize = 192;
        this.ySize = 166;

        this.tile = tile;

        this.rf = tile instanceof TileOvenRF;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(StatCollector.translateToLocal("kc.oven.text" + (rf ? ".rf" : "")), rf ? 20 : 5, 5, 0x404040);

        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

        if (rf)
        {
            List<String> ttLines = new ArrayList<String>();
            if (mouseX < x + 125 && mouseX > x + 50 && mouseY > y + 67 && mouseY < y + 73)
            {
                ttLines.add(((TileOvenRF) this.tile).getEnergyStored(null) + " RF");
                this.func_146283_a(ttLines, mouseX - x, mouseY - y);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
    {
        this.mc.renderEngine.bindTexture(rf ? textureRF : texture);

        x = (this.width - this.xSize) / 2;
        y = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(x, y, 0, 0, this.xSize - 16, this.ySize);

        if (!rf)
        {
            int burn = this.tile.getBurnTimeRemainingScaled(12);

            if (burn != -1)
                this.drawTexturedModalRect(x + 8, y + 42 + 13 - burn, 176, 12 - burn, 14, burn + 2);
        }
        else
        {
            TileOvenRF tilerf = (TileOvenRF) tile;
            int energy = tilerf.getEnergyStored(null) * 70 / tilerf.getMaxEnergyStored(null);

            if (energy != 0)
            {
                this.drawTexturedModalRect(x + 52, y + 67, 176, 5, energy + 1, 6);
            }
        }

        int cook = this.tile.getCookProgressScaled(17);
        this.drawTexturedModalRect(x + 79, y + (rf ? 29 : 26), rf ? 176 : 177, rf ? 1 : 14, cook + 1, rf ? 4 : 16);
    }
}
