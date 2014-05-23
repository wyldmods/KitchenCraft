package org.wyldmods.kitchencraft.machines.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.machines.KitchenCraftMachines;
import org.wyldmods.kitchencraft.machines.client.gui.GuiHandlerKC.GuiTypes;
import org.wyldmods.kitchencraft.machines.tile.TileOven;

public class BlockOven extends BlockContainerKC
{
    private IIcon[] icons;
    
    public BlockOven()
    {
        super("kc.oven", Material.rock, soundTypeStone, 1.5f, 0, TileOven.class);
        setResistance(12.0F);
        
        icons = new IIcon[5];
    }
    
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        icons[0] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + "stove_side");
        icons[1] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + "stove_top");
        icons[2] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + "stove_bottom");
        icons[3] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + "stove_front_off");
        icons[4] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + "stove_front_on");
        
        this.blockIcon = icons[0];
    }
    
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int meta = world.getBlockMetadata(x, y, z);
        
        int face = getFrontFromMeta(meta);
        
        return side == 1 ? icons[1] : side == 0 ? icons[2] : side == face ? world.getBlockMetadata(x, y, z) > 3 ? icons[4] : icons[3] : icons[0];
    }
    
    public static int getFrontFromMeta(int meta)
    {
        meta %= 4;
        return meta == 0 ? 3 : meta == 1 ? 4 : meta == 2 ? 2 : 5;
    }
    
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? icons[1] : side == 0 ? icons[2] : side == 3 ? icons[3] : icons[0];
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
        int meta = world.getBlockMetadata(x, y, z);
        return meta > 3 ? 15 : 0;
    }
}
