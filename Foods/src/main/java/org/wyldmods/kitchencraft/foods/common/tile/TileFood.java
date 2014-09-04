package org.wyldmods.kitchencraft.foods.common.tile;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;
import org.wyldmods.kitchencraft.foods.common.item.KCItems;

public class TileFood extends TileEntity
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
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }

    public ItemStack getFood()
    {
        return food.copy();
    }

    public void setFood(ItemStack stack)
    {
        if (FoodType.getFoodType(stack) != null) this.food = stack.copy();
    }
}
