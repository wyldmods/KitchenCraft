package org.wyldmods.kitchencraft.foods.common.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKCPlant extends BlockCrops implements ITileEntityProvider
{
    private IIcon[] blockIcons;
    private IIcon[] veggieIcons;

    public BlockKCPlant()
    {
        super();
        setBlockTextureName("cropBase");
        setCreativeTab(KitchenCraftFoods.tab);
        GameRegistry.registerBlock(this, "kcCrop");
        GameRegistry.registerTileEntity(TileKCPlant.class, "tileKcCrop");
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
    
    public ItemStack getFood(IBlockAccess world, int x, int y, int z)
    {
        TileKCPlant tile = (TileKCPlant) world.getTileEntity(x, y, z);
        return tile.food == null ? new ItemStack(KCItems.veggie) : tile.food.copy();
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
    
    public static class TileKCPlant extends TileEntity
    {
        private ItemStack food = new ItemStack(KCItems.veggie, 1, 0);
        
        public void setFoodType(String name)
        {
            this.food = FoodType.getFood(name);
        }
        
        public void setFoodType(int damage)
        {
            this.food = new ItemStack(KCItems.veggie, 1, damage);
        }
        
        @Override
        public boolean canUpdate()
        {
            return false;
        }
        
        @Override
        public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta, World world, int x, int y, int z)
        {
            return oldBlock != newBlock;
        }
        
        @Override
        public void writeToNBT(NBTTagCompound tag)
        {
            super.writeToNBT(tag);
            tag.setString("foodType", FoodType.getFoodType(food).name);
        }
        
        @Override
        public void readFromNBT(NBTTagCompound tag)
        {
            super.readFromNBT(tag);
            this.food = FoodType.getFood(tag.getString("foodType"));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileKCPlant();
    }
}
