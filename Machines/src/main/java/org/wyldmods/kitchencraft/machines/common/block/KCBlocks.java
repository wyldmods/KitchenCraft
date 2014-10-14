package org.wyldmods.kitchencraft.machines.common.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;
import org.wyldmods.kitchencraft.machines.common.block.container.BlockOven;
import org.wyldmods.kitchencraft.machines.common.item.block.ItemBlockOven;
import org.wyldmods.kitchencraft.machines.common.tile.TileOven;
import org.wyldmods.kitchencraft.machines.common.tile.TileOvenRF;

import cpw.mods.fml.common.registry.GameRegistry;

public class KCBlocks
{
    public static Block oven;
    public static Block pot;

    public static void init()
    {
        oven = new BlockOven();
        GameRegistry.registerBlock(oven, ItemBlockOven.class, "kc.oven");
        GameRegistry.registerTileEntity(TileOven.class, "kc.oven.fuel");

        if (KitchenCraftMachines.loadRF())
        {
            GameRegistry.registerTileEntity(TileOvenRF.class, "kc.oven.rf");
        }

        pot = new BlockPot();
        GameRegistry.registerBlock(pot, "kc.pot");

        KitchenCraftMachines.tab.setDisplay(Item.getItemFromBlock(oven));

        addRecipes();
    }

    private static void addRecipes()
    {
        /* @formatter:off */
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(oven, 1, 0), 
                "cbc",
                "cFc",
                "cfc",
                
                'c', "cobblestone",
                'b', "barsIron",
                'F', Blocks.furnace,
                'f', new ItemStack(Items.flint_and_steel, 1, OreDictionary.WILDCARD_VALUE)
        ));
        
        
        if (KitchenCraftMachines.loadRF())
        {
            Item machineChassis = GameRegistry.findItem("EnderIO", "itemMachinePart");
            Item capacitor = GameRegistry.findItem("EnderIO", "itemBasicCapacitor");
            ItemStack res = new ItemStack(KCBlocks.oven, 1, 1);

            if (machineChassis == null || capacitor == null)
            {
                GameRegistry.addRecipe(new ShapedOreRecipe(res.copy(), 
                        "ibi",
                        "iri",
                        "ioi",

                        'i', "ingotIron",
                        'b', "barsIron",
                        'r', "blockRedstone",
                        'o', oven
               ));
            }
            else
            {
                GameRegistry.addRecipe(new ShapedOreRecipe(res.copy(), 
                        "sbs",
                        "sos",
                        "scs",

                        's', "stone",
                        'b', "barsIron",
                        'o', oven,
                        'c', capacitor
                ));
            }
        }
    }
}
