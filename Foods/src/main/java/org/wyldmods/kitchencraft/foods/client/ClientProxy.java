package org.wyldmods.kitchencraft.foods.client;

import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.client.render.RenderKCCrop;
import org.wyldmods.kitchencraft.foods.client.render.KCLeavesSpecialRenderer;
import org.wyldmods.kitchencraft.foods.client.render.RenderSeedGrass;
import org.wyldmods.kitchencraft.foods.common.CommonProxy;
import org.wyldmods.kitchencraft.foods.common.block.BlockKCLeaves.TileKCLeaves;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void initRenderers()
    {
        KitchenCraftFoods.renderIDCrop = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderKCCrop());
        
        KitchenCraftFoods.renderIDGrass = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderSeedGrass());
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileKCLeaves.class, new KCLeavesSpecialRenderer());
    }
}
