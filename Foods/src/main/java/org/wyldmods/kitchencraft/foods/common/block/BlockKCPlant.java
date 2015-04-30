package org.wyldmods.kitchencraft.foods.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
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
import org.wyldmods.kitchencraft.foods.common.config.ConfigurationHandler;
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
    
    private IIcon[][] customIcons;

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
        
        customIcons = new IIcon[FoodType.veggies.size()][8];
        for (int i = 0; i < customIcons.length; i++)
        {
            for (int j = 0; j < customIcons[i].length; j++)
            {
                if (FoodType.veggies.get(i).hasCropTexture)
                {
                    customIcons[i][j] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + FoodType.veggies.get(i).name + "_cropStage" + j);
                }
            }
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
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileFood)
        {
            ItemStack food = getFood(world, x, y, z);
            FoodType type = FoodType.getFoodType(food);
            if (type.hasCropTexture)
            {
                return customIcons[food.getItemDamage()][meta];
            }
        }
        return getIcon(0, meta);
    }

    @Override
    public int getRenderType()
    {
        return KitchenCraftFoods.renderIDCrop;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (!FoodType.getFoodType(getFood(world, x, y, z)).canGrowInDimension(world.provider.dimensionId))
        {
            world.setBlockToAir(x, y, z);
        }
        else
        {
            super.updateTick(world, x, y, z, rand);
        }
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
        dropItems(world, x, y, z, meta, false);
        super.breakBlock(world, x, y, z, block, meta);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
            float hitY, float hitZ)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta >= 7 && ConfigurationHandler.allowRightClickHarvest)
        {
            if (!world.isRemote)
            {
                dropItems(world, x, y, z, meta, true);
                world.setBlockMetadataWithNotify(x, y, z, 0, 3);
            }
            return true;
        }
        return false;
    }
    
    private void dropItems(World world, int x, int y, int z, int meta, boolean rc)
    {
        for (ItemStack stack : getDrops(world, x, y, z, meta, rc))
        {
            dropBlockAsItem(world, x, y, z, stack);
        }
    }
    
    public List<ItemStack> getDrops(World world, int x, int y, int z, int meta, boolean rc)
    {
        List<ItemStack> drops = new ArrayList<ItemStack>();
                
        if (meta <= 6)
        {
            drops.add(new ItemStack(KCItems.seed, world.rand.nextInt(Math.max(1, meta / 2)) + 1, getFood(world, x, y, z).getItemDamage())); // add small seed bonus to higher metas
        }
        else
        {
            int dmg = getFood(world, x, y, z).getItemDamage();
            drops.add(new ItemStack(KCItems.veggie, world.rand.nextInt(4) + 1, dmg));
            drops.add(new ItemStack(KCItems.seed, world.rand.nextInt(3) + (rc ? 0 : 1), dmg));
        }
        
        return drops;
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
