package org.wyldmods.kitchencraft.machines.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

import org.wyldmods.kitchencraft.machines.common.tile.TileOven;
import org.wyldmods.kitchencraft.machines.common.tile.TileOvenRF;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerOven extends ContainerKC
{
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private int lastEnergy;

    private TileOven tile;

    private class SlotOvenInput extends Slot
    {
        public SlotOvenInput(IInventory par1iInventory, int par2, int par3, int par4)
        {
            super(par1iInventory, par2, par3, par4);
        }

        @Override
        public boolean isItemValid(ItemStack par1ItemStack)
        {
            return checkInputSlot(par1ItemStack);
        }
    }

    public static boolean checkInputSlot(ItemStack stack)
    {
        return stack != null && FurnaceRecipes.smelting().getSmeltingResult(stack) != null && stack.getItem() instanceof ItemFood;
    }

    private class SlotOvenFuel extends Slot
    {
        public SlotOvenFuel(IInventory par1iInventory, int par2, int par3, int par4)
        {
            super(par1iInventory, par2, par3, par4);
        }

        @Override
        public boolean isItemValid(ItemStack par1ItemStack)
        {
            return checkFuelSlot(par1ItemStack);
        }
    }

    public static boolean checkFuelSlot(ItemStack stack)
    {
        return stack != null && TileEntityFurnace.isItemFuel(stack);
    }

    public ContainerOven(InventoryPlayer invPlayer, TileOven tile)
    {
        super(invPlayer, tile);

        boolean rf = tile instanceof TileOvenRF;

        addSlotToContainer(new SlotOvenInput(tile, 0, 80, rf ? 10 : 8));
        addSlotToContainer(new SlotFurnace(invPlayer.player, tile, 1, 80, rf ? 43 : 55));

        if (!rf)
        {
            addSlotToContainer(new SlotOvenFuel(tile, 2, 8, 59));
        }

        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 >= 36 && par2 <= 38)
            {
                if (!this.mergeItemStack(itemstack1, 27, 36, false))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 27, false))
                        return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            }
            if (par2 < 36)
            {
                if (checkFuelSlot(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 38, 39, false))
                    {
                        if (checkInputSlot(itemstack1) && !this.mergeItemStack(itemstack1, 36, 37, false))
                            return null;
                    }
                }
                else
                {
                    if (checkInputSlot(itemstack1) && !this.mergeItemStack(itemstack1, 36, 37, false))
                        return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }
            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
                return null;
            }
        }
        return itemstack;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.tile.cookTime = par2;
        }

        if (par1 == 1)
        {
            this.tile.burnTime = par2;
        }

        if (par1 == 2)
        {
            this.tile.currentItemBurnTime = par2;
        }
        
        if (par1 == 3)
        {
            ((TileOvenRF)this.tile).setEnergyStored(par2);
        }
    }

    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.lastCookTime != this.tile.cookTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tile.cookTime);
            }

            if (tile instanceof TileOvenRF)
            {
                TileOvenRF tilerf = (TileOvenRF) this.tile;
                if (this.lastEnergy != tilerf.getEnergyStored(null))
                {
                    icrafting.sendProgressBarUpdate(this, 3, tilerf.getEnergyStored(null));
                }
                this.lastEnergy = tilerf.getEnergyStored(null);
            }
            else
            {
                if (this.lastBurnTime != this.tile.burnTime)
                {
                    icrafting.sendProgressBarUpdate(this, 1, this.tile.burnTime);
                }

                if (this.lastItemBurnTime != this.tile.currentItemBurnTime)
                {
                    icrafting.sendProgressBarUpdate(this, 2, this.tile.currentItemBurnTime);
                }
            }
        }

        this.lastCookTime = this.tile.cookTime;
        this.lastBurnTime = this.tile.burnTime;
        this.lastItemBurnTime = this.tile.currentItemBurnTime;
    }
}
