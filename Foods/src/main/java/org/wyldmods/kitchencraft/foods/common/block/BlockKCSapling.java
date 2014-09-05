package org.wyldmods.kitchencraft.foods.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.tile.TileFood;
import org.wyldmods.kitchencraft.foods.common.world.WorldGenFruitTree;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKCSapling extends BlockSapling implements IKCPlant
{
    public BlockKCSapling()
    {
        super();
        setCreativeTab(KitchenCraftFoods.tab);
        setStepSound(soundTypeGrass);
        setBlockName("kc.sapling");
        setBlockTextureName(Reference.MOD_TEXTUREPATH + ":sapling");
        GameRegistry.registerBlock(this, "sapling");
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List list)
    {
        list.add(new ItemStack(this));
    }
    
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon(Reference.MOD_TEXTUREPATH + ":sapling");
    }
    
    @Override
    public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_)
    {
        return this.blockIcon;
    }
    
    @Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return this.blockIcon;
    }

    @Override
    public int damageDropped(int meta)
    {
        return 0;
    }

    @Override
    // grow tree
    public void func_149878_d(World world, int x, int y, int z, Random rand)
    {
        if (new WorldGenFruitTree(FoodType.getFoodType(getFood(world, x, y, z))).generate(world, rand, x, y, z))
        {
            world.setBlockMetadataWithNotify(x, y - 1, z, 15, 3);
            world.setBlock(x, y - 1, z, Blocks.grass, 0, 3);
        }
    }
    
    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
        return world.getBlock(x, y - 1, z) == KCBlocks.seedGrass;
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return world.getBlock(x, y - 1, z) == KCBlocks.seedGrass;
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        return new ArrayList<ItemStack>();
    }

    @Override
    public ItemStack getFood(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y - 1, z);
        if (te != null)
        {
            return ((TileFood)te).getFood();
        }
        return null;
    }

    @Override
    public void setFood(ItemStack stack, IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y - 1, z);
        if (te != null)
        {
            ((TileFood)te).setFood(stack);
        }
    }

    @Override
    public String getSuffix()
    {
        return KitchenCraftFoods.lang.localize("tooltip.sapling");
    }
}
