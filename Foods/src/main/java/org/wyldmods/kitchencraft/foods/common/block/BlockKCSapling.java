package org.wyldmods.kitchencraft.foods.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.world.WorldGenFruitTree;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKCSapling extends BlockSapling
{
    public static final WorldGenFruitTree gen = new WorldGenFruitTree();

    public BlockKCSapling()
    {
        super();
        setCreativeTab(KitchenCraftFoods.tab);
        GameRegistry.registerBlock(this, "sapling");
        setStepSound(soundTypeGrass);
        setBlockName("kc.sapling");
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
    public int damageDropped(int meta)
    {
        return 0;
    }

    @Override
    // grow tree
    public void func_149878_d(World world, int x, int y, int z, Random rand)
    {
        gen.generate(world, rand, x, y, z);
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
}
