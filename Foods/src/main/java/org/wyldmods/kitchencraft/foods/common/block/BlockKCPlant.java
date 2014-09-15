package org.wyldmods.kitchencraft.foods.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;
import org.wyldmods.kitchencraft.foods.common.tile.TileFood;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKCPlant extends BlockCrops implements ITileEntityProvider, IKCPlant
{
    private IIcon[] blockIcons;
    private IIcon[] veggieIcons;

    public BlockKCPlant()
    {
        super();
        setBlockTextureName("cropBase");
        setBlockName("kc.crop");
        setCreativeTab(KitchenCraftFoods.tab);
        GameRegistry.registerBlock(this, "crop");
        GameRegistry.registerTileEntity(TileFood.class, "tileFood");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        blockIcons = new IIcon[8];
        for (int i = 0; i < blockIcons.length; i++)
        {
            blockIcons[i] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":cropBase" + i);
        }
        
        veggieIcons = new IIcon[FoodType.veggies.size()];
        for (int i = 0; i < veggieIcons.length; i++)
        {
            veggieIcons[i] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + FoodType.veggies.get(i).name);
        }
    }
    
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return blockIcons[meta % blockIcons.length];
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        int meta = world.getBlockMetadata(x, y, z);
        return getIcon(0, meta);
    }

    @Override
    public int getRenderType()
    {
        return KitchenCraftFoods.renderIDCrop;
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
        if (tile != null) tile.setFood(stack);
    }
    
    @Override
    public String getSuffix()
    {
        return KitchenCraftFoods.lang.localize("tooltip.plant");
    }
    
    public IIcon getFoodIcon(IBlockAccess world, int x, int y, int z)
    {
        FoodType type = FoodType.getFoodType(getFood(world, x, y, z));
        return veggieIcons[FoodType.veggies.indexOf(type)];
    }
    
    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return null;
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        if (meta <= 6)
        {
            dropBlockAsItem(world, x, y, z, new ItemStack(KCItems.seed, world.rand.nextInt(Math.max(1, meta / 2)) + 1, getFood(world, x, y, z).getItemDamage())); // add small seed bonus to higher metas
        }
        else
        {
            int dmg = getFood(world, x, y, z).getItemDamage();
            dropBlockAsItem(world, x, y, z, new ItemStack(KCItems.veggie, world.rand.nextInt(4) + 1, dmg));
            dropBlockAsItem(world, x, y, z, new ItemStack(KCItems.seed, world.rand.nextInt(3) + 1, dmg));
        }
        
        super.breakBlock(world, x, y, z, block, meta);
    }
    
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Crop;
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        return new ArrayList<ItemStack>();
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileFood();
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileFood)
        {
            return new ItemStack(KCItems.seed, 1, ((TileFood)te).getFood().getItemDamage());
        }
        return new ItemStack(KCItems.seed);
    }
}
