package org.wyldmods.kitchencraft.machines.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.WavefrontObject;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;
import org.wyldmods.kitchencraft.machines.client.render.SimpleModelRenderer;
import org.wyldmods.kitchencraft.machines.common.CommonProxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static SimpleModelRenderer pot;
    
    @Override
    public void initRenderers()
    {
        pot = new SimpleModelRenderer(new WavefrontObject(new ResourceLocation(Reference.MOD_TEXTUREPATH, "models/pot.obj")));
        KitchenCraftMachines.renderIDPot = pot.getRenderId();
        RenderingRegistry.registerBlockHandler(pot);
    }
}
