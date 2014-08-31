package org.wyldmods.kitchencraft.machines.common.tile;

import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.EnumSkyBlock;

import org.wyldmods.kitchencraft.machines.common.container.ContainerOven;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileOven extends TileKCInventory implements ISidedInventory
{
    public int cookTime;
    public int burnTime;
    public int currentItemBurnTime;

    private boolean isBurning;
    private boolean isCooking;

    protected int maxCookTime = 159;
    protected int maxBurnTime = 200;
    
    private int uncooked = 0, cooked = 1, fuel = 2;

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
                if (!useFuel())
                {
                    isBurning = canCook() && consumeFuel();
                    needsSync = true;
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
            if (isActive() && !isBurning)
            {
                updateMeta(meta - 4);
                needsSync = true;
            }
            else if (!isActive() && isBurning)
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
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[uncooked]);

            if (this.inventory[cooked] == null)
            {
                this.inventory[cooked] = itemstack.copy();
            }
            else if (this.inventory[cooked].getItem() == itemstack.getItem())
            {
                this.inventory[cooked].stackSize += itemstack.stackSize;
            }

            --this.inventory[uncooked].stackSize;

            if (this.inventory[uncooked].stackSize <= 0)
            {
                this.inventory[uncooked] = null;
            }
        }
    }

    protected boolean canCook()
    {
        if (this.inventory[uncooked] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[uncooked]);
            if (itemstack == null || !isValidFood(itemstack))
                return false;
            if (this.inventory[cooked] == null)
                return true;
            if (!this.inventory[cooked].isItemEqual(itemstack))
                return false;
            int result = inventory[cooked].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.inventory[cooked].getMaxStackSize();
        }
    }

    protected boolean consumeFuel()
    {
        if (TileEntityFurnace.isItemFuel(inventory[fuel]))
        {
            inventory[fuel].stackSize--;
            currentItemBurnTime = TileEntityFurnace.getItemBurnTime(inventory[fuel]);
            burnTime = currentItemBurnTime;
            if (inventory[fuel].stackSize == 0)
                this.inventory[fuel] = inventory[fuel].getItem().getContainerItem(inventory[fuel]);

            return true;
        }
        return false;
    }
    
    protected boolean useFuel()
    {
        if (burnTime > 0)
        {
            burnTime--;
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
    
    // BASED ON METADATA ONLY, used for packet sending checks
    private boolean isActive()
    {
        return getBlockMetadata() % 8 > 3;
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
            return new int[] { cooked, fuel };
        case 1:
            return new int[] { uncooked };
        default:
            return new int[] { fuel };
        }
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return slot == cooked ? false : (slot == fuel ? ContainerOven.checkFuelSlot(stack): ContainerOven.checkInputSlot(stack));
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        return side != uncooked || slot != fuel || stack.getItem() == Items.bucket;
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
