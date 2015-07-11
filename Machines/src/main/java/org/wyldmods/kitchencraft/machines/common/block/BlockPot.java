package org.wyldmods.kitchencraft.machines.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;

public class BlockPot extends BlockKC
{
    public BlockPot()
    {
        super("kc.pot", Material.iron, soundTypeMetal, 2f, KitchenCraftMachines.renderIDPot);
    }
    
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + "pot");
    }
}
