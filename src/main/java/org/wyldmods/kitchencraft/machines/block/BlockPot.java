package org.wyldmods.kitchencraft.machines.block;

import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;

import net.minecraft.block.material.Material;

public class BlockPot extends BlockKC
{
    public BlockPot()
    {
        super("kc.pot", Material.iron, soundTypeMetal, 2f, KitchenCraftMachines.renderIDPot);
    }
}
