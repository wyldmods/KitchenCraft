package org.wyldmods.kitchencraft.machines.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.wyldmods.kitchencraft.machines.common.config.ConfigHandler;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class TileOvenRF extends TileOven implements IEnergyHandler
{
    EnergyStorage storage = new EnergyStorage(20000);
    
    public TileOvenRF()
    {
        super();
        this.maxCookTime = 40;
    }
    
    @Override
    protected boolean consumeFuel()
    {
        return storage.getEnergyStored() >= getEnergyUsage();
    }
    
    @Override
    protected boolean useFuel()
    {
        if (inventory[uncooked] != null && storage.getEnergyStored() >= getEnergyUsage())
        {
            storage.extractEnergy(getEnergyUsage(), false);
            return true;
        }
        return false;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return true;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return storage.getEnergyStored();
    }
    

    public void setEnergyStored(int energy)
    {
        storage.setEnergyStored(energy);   
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return storage.getMaxEnergyStored();
    }
    
    public int getEnergyUsage()
    {
        return ConfigHandler.ovenUsePerTick;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("energyStored", storage.getEnergyStored());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        storage.setEnergyStored(nbt.getInteger("energyStored"));
    }
}
