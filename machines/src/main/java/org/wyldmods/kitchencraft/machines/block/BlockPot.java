package org.wyldmods.kitchencraft.machines.block;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

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
