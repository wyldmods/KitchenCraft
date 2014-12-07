package org.wyldmods.kitchencraft.machines.common.config;

import org.wyldmods.kitchencraft.common.lib.Reference;

import tterrag.core.common.config.AbstractConfigHandler;

public class ConfigHandler extends AbstractConfigHandler
{
    public static final ConfigHandler INSTANCE = new ConfigHandler();
    
    public static int ovenUsePerTick = 20;
    
    private ConfigHandler()
    {
        super(Reference.MOD_ID_MACHINES);
    }

    @Override
    protected void init()
    {
        addSection("oven");
    }

    @Override
    protected void reloadNonIngameConfigs()
    {
        ;
    }

    @Override
    protected void reloadIngameConfigs()
    {
        ovenUsePerTick = getValue("ovenUsePerTick", "Usage of RF oven in RF/t", ovenUsePerTick);
    }
}
