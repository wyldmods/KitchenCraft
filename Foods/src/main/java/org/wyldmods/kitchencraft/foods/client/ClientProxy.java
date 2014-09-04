package org.wyldmods.kitchencraft.foods.client;

import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.client.render.RenderKCCrop;
import org.wyldmods.kitchencraft.foods.client.render.RenderSeedGrass;
import org.wyldmods.kitchencraft.foods.common.CommonProxy;

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
    }
}
