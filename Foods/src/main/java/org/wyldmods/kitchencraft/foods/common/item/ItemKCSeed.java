package org.wyldmods.kitchencraft.foods.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.block.BlockKCPlant.TileKCPlant;
import org.wyldmods.kitchencraft.foods.common.block.KCBlocks;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;

public class ItemKCSeed extends Item implements IPlantable
{
    public ItemKCSeed()
    {
        super();
        setCreativeTab(KitchenCraftFoods.tab);
        setHasSubtypes(true);
        setTextureName(Reference.MOD_TEXTUREPATH + ":seed");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < FoodType.veggies.size(); i++)
        {
            FoodType type = FoodType.veggies.get(i);
            if (type.makeSeed)
            {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    public void initSeedRegistrations()
    {
        for (int i = 0; i < FoodType.veggies.size(); i++)
        {
            FoodType type = FoodType.veggies.get(i);
            if (type.makeSeed)
            {
                MinecraftForge.addGrassSeed(new ItemStack(this, 1, i), 10);
            }
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int pass)
    {
        return FoodType.veggies.get(stack.getItemDamage()).color;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return KCItems.veggie.getUnlocalizedName(stack) + "Seed";
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        Block placedOn = world.getBlock(x, y, z);
        if (placedOn.canSustainPlant(world, x, y, z, ForgeDirection.UP, this) && side == ForgeDirection.UP.ordinal())
        {
            y++; // move it on up
            world.setBlock(x, y, z, KCBlocks.crop, 0, 0);
            TileKCPlant te = (TileKCPlant) world.getTileEntity(x, y, z);
            te.setFoodType(stack.getItemDamage());
            world.markBlockForUpdate(x, y, z);
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
            return true;
        }
        return false;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z)
    {
        return KCBlocks.crop;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
    {
        return 0;
    }
}
