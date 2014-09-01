package org.wyldmods.kitchencraft.machines.common.block.container;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
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
import org.wyldmods.kitchencraft.machines.common.tile.TileOven;
import org.wyldmods.kitchencraft.machines.common.tile.TileOvenRF;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOven extends BlockContainerKC
{
    private IIcon[][] icons; // [type][side]

    public BlockOven()
    {
        super("kc.oven", Material.rock, soundTypeStone, 1.5f, 0, null);
        setResistance(15.0F);

        icons = new IIcon[2][5];
    }

    private static final String[] suffixes = { "side", "top", "bottom", "front_off", "front_on" };

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        for (int type = 0; type <= 1; type++)
            for (int idx = 0; idx <= 4; idx++)
                icons[type][idx] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + "oven_" + suffixes[idx] + (type == 1 ? "_RF" : ""));
        // this ends up with stove_top or stove_top_RF etc.

        this.blockIcon = icons[0][0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int meta = world.getBlockMetadata(x, y, z);

        int face = getFrontFromMeta(meta);

        IIcon[] icons = this.icons[getType(meta)];

        meta %= 8;

        return side == 1 ? icons[1] : side == 0 ? icons[2] : side == face ? meta > 3 ? icons[4] : icons[3] : icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (meta > 1) meta = getType(meta);
        return side == 1 ? icons[meta][1] : side == 0 ? icons[meta][2] : side == 3 ? icons[meta][3] : icons[meta][0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
    {
        // screw you mojang

        int max = 4;

        for (int xOffset = 0; xOffset < max; ++xOffset)
        {
            for (int yOffset = 0; yOffset < max; ++yOffset)
            {
                for (int zOffset = 0; zOffset < max; ++zOffset)
                {
                    double pX = (double) x + ((double) xOffset + 0.5D) / (double) max;
                    double pY = (double) y + ((double) yOffset + 0.5D) / (double) max;
                    double pZ = (double) z + ((double) zOffset + 0.5D) / (double) max;
                    Minecraft.getMinecraft().effectRenderer.addEffect((new EntityDiggingFX(world, pX, pY, pZ, pX - (double) x - 0.5D, pY - (double) y - 0.5D, pZ - (double) z - 0.5D, this, 
                            /* THE IMPORTANT THING -> */ getType(meta))).applyColourMultiplier(x, y, z));
                }
            }
        }

        return true;
    }

    public static int getFrontFromMeta(int meta)
    {
        meta %= 4;
        return meta == 0 ? 3 : meta == 1 ? 4 : meta == 2 ? 2 : 5;
    }

    public int getType(World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        return getType(meta);
    }

    public int getType(int meta)
    {
        return meta % 8 == meta ? 0 : 1;
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
        int type = getType(par1World, x, y, z);
        par1World.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing + (type * 8), 2);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        int meta = world.getBlockMetadata(x, y, z) % 8;

        if (meta > 3)
        {
            int side = getFrontFromMeta(meta);
            float pX = (float) x + 0.5F;
            float pY = (float) y + 0.1F + rand.nextFloat() * 6.0F / 16.0F;
            float pZ = (float) z + 0.5F;
            float offset1 = 0.52F;
            float offset2 = rand.nextFloat() * 0.6F - 0.3F;

            int circle = rand.nextInt(4);

            if (side == 4)
            {
                world.spawnParticle("flame", (double) (pX - offset1), (double) pY, (double) (pZ + offset2), 0.0D, 0.0D, 0.0D);
            }
            else if (side == 5)
            {
                world.spawnParticle("flame", (double) (pX + offset1), (double) pY, (double) (pZ + offset2), 0.0D, 0.0D, 0.0D);
            }
            else if (side == 2)
            {
                world.spawnParticle("flame", (double) (pX + offset2), (double) pY, (double) (pZ - offset1), 0.0D, 0.0D, 0.0D);
            }
            else if (side == 3)
            {
                world.spawnParticle("flame", (double) (pX + offset2), (double) pY, (double) (pZ + offset1), 0.0D, 0.0D, 0.0D);
            }

            pX = x + (circle < 2 ? .25F : .75F);
            pZ = z + (circle == 1 || circle == 3 ? .25F : .75f);

            world.spawnParticle("smoke", (double) pX, (double) y + 1.01, (double) pZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z) % 8;
        return meta > 3 ? 15 : 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return metadata < 8 ? new TileOven() : new TileOvenRF();
    }
}
