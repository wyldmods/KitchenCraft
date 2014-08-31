package org.wyldmods.kitchencraft.machines.common.tile;

import org.wyldmods.kitchencraft.machines.common.container.ContainerOven;

import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.EnumSkyBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileOven extends TileKCInventory implements ISidedInventory
{
    public int cookTime;
    public int burnTime;
    public int currentItemBurnTime;

    private boolean isBurning;
    private boolean isCooking;

    private final int maxCookTime = 159;
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
            // if fuel is being used
            if (isBurning)
            {
                // make sure we are not out of fuel, consume more fuel if there is more to cook
                if (burnTime == 0)
                {
                    isBurning = canCook() && consumeFuel();
                    needsSync = true;
                }
                // else decrease the fuel
                else
                {
                    burnTime--;
                }

                // if we are cooking something, and are still able to
                if (isCooking && canCook())
                {
                    // if we have reached the completion of an item, smelt it
                    if (cookTime == maxCookTime)
                    {
                        smeltItem();
                        cookTime = 0;
                        needsSync = true;
                    }
                    // otherwise, progress the bar
                    else
                    {
                        cookTime++;
                    }
                }
                // otherwise, check if we are able to cook
                else 
                {
                    isCooking = canCook();
                }
            }
            // if no fuel is being burned, see if it can be, and subsequently if we can cook
            else
            {
                isBurning = canCook() && consumeFuel();
                isCooking = canCook();
            }
            
            // reset the progress if cooking is halted
            if (!isCooking)
                cookTime = 0;
            
            // this block updates the lighting and metadata between on/off states
            int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            if (meta > 3 && !isBurning)
            {
                updateMeta(meta - 4);
                needsSync = true;
            }
            else if (meta < 4 && isBurning)
            {
                updateMeta(meta + 4);
                needsSync = true;
            }
            
            // finally, sync any changes made
            if (needsSync)
            {
                markDirty();
            }
        }
    }

    private void updateMeta(int meta)
    {
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 3);
        worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
        worldObj.notifyBlockChange(xCoord, yCoord, zCoord, getBlockType());
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
        
        if (this.burnTime == 0)
        {
            return -1;
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
            return new int[] { 2, 1 };
        case 1:
            return new int[] { 0 };
        default:
            return new int[] { 1 };
        }
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return slot == 2 ? false : (slot == 1 ? ContainerOven.checkFuelSlot(stack): ContainerOven.checkInputSlot(stack));
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        return side != 0 || slot != 1 || stack.getItem() == Items.bucket;
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
