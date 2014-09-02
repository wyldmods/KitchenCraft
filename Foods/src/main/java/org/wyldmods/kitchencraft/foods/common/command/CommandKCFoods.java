package org.wyldmods.kitchencraft.foods.common.command;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import org.wyldmods.kitchencraft.foods.common.config.ConfigurationHandler;

public class CommandKCFoods extends CommandBase
{
    public static final String[] ARGS = {"reloadJson"};
    
    @Override
    public String getCommandName()
    {
        return "kcf";
    }

    @Override
    public String getCommandUsage(ICommandSender player)
    {
        return "/kcf [arg], valid args: " + Arrays.deepToString(ARGS);
    }

    @Override
    public void processCommand(ICommandSender player, String[] args)
    {
        if ("reloadJson".equals(args[0]))
        {
            ConfigurationHandler.reload();
        }
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender player, String[] args)
    {
        return getListOfStringsMatchingLastWord(args, ARGS);
    }
}
