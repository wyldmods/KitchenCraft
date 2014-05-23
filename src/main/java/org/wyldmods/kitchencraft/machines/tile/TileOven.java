package org.wyldmods.kitchencraft.machines.tile;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileOven extends TileKCInventory implements ISidedInventory
{
    public int cookTime;
    public int burnTime;
    public int currentItemBurnTime;

    private boolean isBurning;
    private boolean isCooking;

    private final int maxCookTime = (int) (200 - (200 * .2));
    private final int maxBurnTime = 200;

    public TileOven()
    {
        super(3);
    }

    @Override
    public void updateEntity()
    {
        boolean needsSync = false;

        if (!worldObj.isRemote)
        {
            if (isBurning)
            {
                if (burnTime == 0)
                {
                    isBurning = consumeFuel();
                    needsSync = true;
                }
                else
                {
                    burnTime--;
                }

                if (isCooking && canCook())
                {
                    if (cookTime == maxCookTime)
                    {
                        smeltItem();
                        cookTime = 0;
                        needsSync = true;
                    }
                    else
                    {
                        cookTime++;
                    }
                }
                else 
                {
                    isCooking = canCook();
                }
            }
            else
            {
                isBurning = canCook() && consumeFuel();
                isCooking = canCook();
            }
            
            if (!isCooking)
                cookTime = 0;
            
            if (needsSync)
            {
                markDirty();
            }
        }
    }

    private void smeltItem()
    {
        if (this.canCook())
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]);

            if (this.inventory[2] == null)
            {
                this.inventory[2] = itemstack.copy();
            }
            else if (this.inventory[2].getItem() == itemstack.getItem())
            {
                this.inventory[2].stackSize += itemstack.stackSize;
            }

            --this.inventory[0].stackSize;

            if (this.inventory[0].stackSize <= 0)
            {
                this.inventory[0] = null;
            }
        }
    }

    private boolean canCook()
    {
        if (this.inventory[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]);
            if (itemstack == null || !isValidFood(itemstack))
                return false;
            if (this.inventory[2] == null)
                return true;
            if (!this.inventory[2].isItemEqual(itemstack))
                return false;
            int result = inventory[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.inventory[2].getMaxStackSize();
        }
    }

    private boolean consumeFuel()
    {
        if (TileEntityFurnace.isItemFuel(inventory[1]))
        {
            inventory[1].stackSize--;
            currentItemBurnTime = TileEntityFurnace.getItemBurnTime(inventory[1]);
            burnTime = currentItemBurnTime;
            if (inventory[1].stackSize == 0)
                this.inventory[1] = inventory[1].getItem().getContainerItem(inventory[1]);

            return true;
        }
        return false;
    }

    private boolean isValidFood(ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof ItemFood;
    }

    public boolean isBurning()
    {
        return isBurning;
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int scale)
    {
        return this.cookTime * scale / maxCookTime;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int scale)
    {
        if (this.currentItemBurnTime == 0)
        {
            this.currentItemBurnTime = maxBurnTime;
        }

        return this.burnTime * scale / this.currentItemBurnTime;
    }

    @Override
    public String getInventoryName()
    {
        return "kc.oven";
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        switch (var1)
        {
        case 0:
            return new int[] { 1 };
        case 1:
            return new int[] { 0 };
        default:
            return new int[] { 2 };
        }
    }

    @Override
    public boolean canInsertItem(int var1, ItemStack var2, int var3)
    {
        return var1 < 2 ? var1 == 1 ? TileEntityFurnace.isItemFuel(var2) : true : false;
    }

    @Override
    public boolean canExtractItem(int var1, ItemStack var2, int var3)
    {
        return true;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        
        nbt.setInteger("burnTime", burnTime);
        nbt.setInteger("cookTime", cookTime);
        nbt.setInteger("curBurnTime", currentItemBurnTime);
        
        nbt.setBoolean("isBurning", isBurning);
        nbt.setBoolean("isCooking", isCooking);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        
        this.burnTime = nbt.getInteger("burnTime");
        this.cookTime = nbt.getInteger("cookTime");
        this.currentItemBurnTime = nbt.getInteger("curBurnTime");
        
        this.isBurning = nbt.getBoolean("isBurning");
        this.isCooking = nbt.getBoolean("isCooking");
    }
}
