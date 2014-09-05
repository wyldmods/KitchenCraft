package org.wyldmods.kitchencraft.foods.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;
import org.wyldmods.kitchencraft.foods.common.tile.TileFood;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSeedGrass extends Block implements ITileEntityProvider, IKCPlant, IGrowable
{
    private IIcon seedHole;
    
    public BlockSeedGrass()
    {
        super(Material.grass);
        setStepSound(soundTypeGrass);
        setHardness(0.6F);
        setBlockName("kc.seedGrass");
        setTickRandomly(true);
        GameRegistry.registerBlock(this, "seedGrass");
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        seedHole = register.registerIcon(Reference.MOD_TEXTUREPATH + ":seedGrass");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? seedHole : Blocks.grass.getIcon(side, meta);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        grow(world, x, y, z);
    }

    @Override
    public int getRenderType()
    {
        return KitchenCraftFoods.renderIDGrass;
    }

    @Override
    public ItemStack getFood(IBlockAccess world, int x, int y, int z)
    {
        TileFood tile = (TileFood) world.getTileEntity(x, y, z);
        return tile != null ? tile.getFood() : new ItemStack(KCItems.veggie);
    }

    @Override
    public void setFood(ItemStack stack, IBlockAccess world, int x, int y, int z)
    {
        TileFood tile = (TileFood) world.getTileEntity(x, y, z);
        if (tile != null)
            tile.setFood(stack);
    }

    @Override
    public String getSuffix()
    {
        return KitchenCraftFoods.lang.localize("tooltip.seed");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileFood();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        return Blocks.grass.getDrops(world, x, y, z, metadata, fortune);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        if (meta != 15) // will be set by sapling
        {
            dropBlockAsItem(world, x, y, z, new ItemStack(KCItems.seed, 1, getFood(world, x, y, z).getItemDamage()));
            super.breakBlock(world, x, y, z, block, meta);
        }
    }

    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isRemote)
    {
        return world.isAirBlock(x, y + 1, z);
    }

    @Override
    public boolean func_149852_a(World world, Random rand, int x, int y, int z)
    {
        return rand.nextBoolean();
    }

    @Override
    public void func_149853_b(World world, Random rand, int x, int y, int z)
    {
        grow(world, x, y, z);
    }

    private void grow(World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 1 && world.rand.nextBoolean() && world.isAirBlock(x, y + 1, z))
        {
            world.playAuxSFX(2005, x, y + 1, z, 0);
            world.setBlock(x, y + 1, z, KCBlocks.sapling, 0, 3);
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, 1, 3);
        }
    }
}