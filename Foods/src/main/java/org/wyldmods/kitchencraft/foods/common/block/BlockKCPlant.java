package org.wyldmods.kitchencraft.foods.common.block;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKCPlant extends BlockCrops implements ITileEntityProvider
{
    private IIcon[] icons;

    public BlockKCPlant()
    {
        super();
        setBlockTextureName("cropBase");
        setCreativeTab(KitchenCraftFoods.tab);
        GameRegistry.registerBlock(this, "kcCrop");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
        icons = new IIcon[6];
        for (int i = 0; i < icons.length; i++)
        {
            icons[i] = register.registerIcon(Reference.MOD_TEXTUREPATH + ":cropBase" + i);
        }
    }
    
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return icons[meta % icons.length];
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
    
    public static class TileKCPlant extends TileEntity
    {
        private ItemStack food;
        
        public void setFoodType(String name)
        {
            this.food = FoodType.getFood(name);
        }
        
        public void setFoodType(int damage)
        {
            this.food = new ItemStack(KCItems.veggie, 1, damage);
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
