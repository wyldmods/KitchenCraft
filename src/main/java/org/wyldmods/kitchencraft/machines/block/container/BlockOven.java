package org.wyldmods.kitchencraft.machines.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;
import org.wyldmods.kitchencraft.machines.client.gui.GuiHandlerKC.GuiTypes;
import org.wyldmods.kitchencraft.machines.tile.TileOven;

public class BlockOven extends BlockContainerKC
{
    public BlockOven()
    {
        super("kc.oven", Material.rock, soundTypeStone, 1.0f, 0, TileOven.class);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileOven)
        {
            player.openGui(KitchenCraftMachines.instance, GuiTypes.OVEN.ordinal(), world, x, y, z);
            return true;
        }
        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }
    
    @Override
    public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int whichDirectionFacing = MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F + 2.5D) & 3;
        par1World.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z) > 3 ? 15 : 0;
    }
}
