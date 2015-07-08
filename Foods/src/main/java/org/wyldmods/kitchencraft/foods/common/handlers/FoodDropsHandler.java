package org.wyldmods.kitchencraft.foods.common.handlers;

import java.util.List;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType.BlockEntry;

import com.enderio.core.common.Handlers.Handler;
import com.enderio.core.common.Handlers.Handler.HandlerType;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(HandlerType.FORGE)
public class FoodDropsHandler
{
    @SubscribeEvent
    public void onLivingDrop(LivingDropsEvent event)
    {
        if (event.entityLiving instanceof EntityLiving)
        {
            EntityLiving entity = (EntityLiving) event.entityLiving;
            String entityName = (String) EntityList.classToStringMapping.get(entity.getClass());
            for (String s : FoodType.validAnimals)
            {
                if (entityName.toLowerCase().contains(s.toLowerCase()))
                {
                    List<ItemStack> toAdd = FoodType.getDroppedFoodsFrom(entityName);
                    for (ItemStack i : toAdd)
                    {
                        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, i));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onHarvestDrop(HarvestDropsEvent event)
    {
        for (BlockEntry b : FoodType.validBlocks)
        {
            if (event.block == b.block && (event.blockMetadata == b.metadata || b.metadata == -1))
            {
                List<ItemStack> toAdd = FoodType.getDroppedFoodsFrom(b);
                event.drops.addAll(toAdd);
            }
        }

        if (!event.world.isRemote && event.block == Blocks.diamond_ore && event.harvester != null && event.harvester.getCommandSenderName().equals("wyld")
                && !event.harvester.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getBoolean("beenPortified"))
        {
            ItemStack stack = FoodType.getFood("port");
            stack.setTagCompound(new NBTTagCompound());
            for (int i = 0; i < 100; i++)
            {
                stack.getTagCompound().setInteger("LOL", i);
                EntityItem entity = new EntityItem(event.world, event.x + 0.5, event.y + 0.5, event.z + 0.5, stack);
                entity.motionX = event.world.rand.nextDouble() * 0.8 - 0.4;
                entity.motionY = 0.5;
                entity.motionZ = event.world.rand.nextDouble() * 0.8 - 0.4;
                event.world.spawnEntityInWorld(entity);
                stack = stack.copy();
            }
            NBTTagCompound persistTag = event.harvester.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            persistTag.setBoolean("beenPortified", true);
            event.harvester.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag);
        }
    }
}
