package org.wyldmods.kitchencraft.machines.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class TileOvenRF extends TileOven implements IEnergyHandler
{
    EnergyStorage storage = new EnergyStorage(20000);
    private static final int usagePerTick = 80;
    
    public TileOvenRF()
    {
        super();
        this.maxCookTime = 40;
    }
    
    @Override
    protected boolean consumeFuel()
    {
        return storage.getEnergyStored() >= usagePerTick;
    }
    
    @Override
    protected boolean useFuel()
    {
        if (storage.getEnergyStored() >= usagePerTick)
        {
            storage.extractEnergy(usagePerTick, false);
            System.out.println(storage.getEnergyStored());
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

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return storage.getMaxEnergyStored();
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
