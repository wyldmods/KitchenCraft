package org.wyldmods.kitchencraft.foods.common.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.common.lib.Reference;
import org.wyldmods.kitchencraft.foods.KitchenCraftFoods;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;
import org.wyldmods.kitchencraft.foods.common.tile.TileFood;

import scala.util.Random;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKCLeaves extends BlockLeaves implements ITileEntityProvider, IKCPlant
{
    public BlockKCLeaves()
    {
        super();
        setCreativeTab(KitchenCraftFoods.tab);
        setBlockName("kc.leaves");
        setBlockTextureName(Reference.MOD_TEXTUREPATH + ":leaves");
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int meta)
    {
        return this == KCBlocks.fruityLeaves ? new TileKCLeaves() : null;
    }

    @Override
    public ItemStack getFood(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileFood)
        {
            return ((TileFood) te).getFood();
        }
        return null;
    }

    @Override
    public void setFood(ItemStack stack, IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileFood)
        {
            ((TileFood) te).setFood(stack);
        }
    }

    @Override
    public String getSuffix()
    {
        return KitchenCraftFoods.lang.localize("tooltip.leaves");
    }

    @Override
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return this.blockIcon;
    }

    @Override
    public String[] func_150125_e()
    {
        return new String[] { "fruitLeaves" };
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        return new ArrayList<ItemStack>();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileKCLeaves)
        {
            int dmg = getFood(world, x, y, z).getItemDamage();
            for (int i = 0; i < ((TileKCLeaves)te).amnt; i++) // dropping all in one stack is lame
            {
                dropBlockAsItem(world, x, y, z, new ItemStack(KCItems.veggie, 1, dmg));
            }
        }
    }

    private static final Random rand = new Random();

    public static final class TileKCLeaves extends TileFood { public TileKCLeaves() {} public final int amnt = rand.nextInt(4) + 1; } // this exists for TESR purposes
}
