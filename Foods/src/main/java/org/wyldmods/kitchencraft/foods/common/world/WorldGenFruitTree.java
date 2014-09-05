package org.wyldmods.kitchencraft.foods.common.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import org.wyldmods.kitchencraft.foods.common.block.IKCPlant;
import org.wyldmods.kitchencraft.foods.common.block.KCBlocks;
import org.wyldmods.kitchencraft.foods.common.config.json.FoodType;

public class WorldGenFruitTree extends WorldGenAbstractTree
{
    private int minTreeHeight = 3;
    private FoodType type;

    public WorldGenFruitTree(FoodType type)
    {
        super(true);
        this.type = type;
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z)
    {
        int height = rand.nextInt(3) + this.minTreeHeight;
        boolean canGrow = true;

        if (y >= 1 && y + height + 1 <= 256)
        {
            int width;
            int yPos;

            for (int posY = y; posY <= y + 1 + height; ++posY)
            {
                width = 1;

                if (posY == y)
                {
                    width = 0;
                }

                if (posY >= y + 1 + height - 2)
                {
                    width = 2;
                }

                for (int posX = x - width; posX <= x + width && canGrow; ++posX)
                {
                    for (yPos = z - width; yPos <= z + width && canGrow; ++yPos)
                    {
                        if (posY >= 0 && posY < 256)
                        {
                            if (!this.isReplaceable(world, posX, posY, yPos))
                            {
                                canGrow = false;
                            }
                        }
                        else
                        {
                            canGrow = false;
                        }
                    }
                }
            }

            if (!canGrow)
            {
                return false;
            }
            else
            {
                Block ground = world.getBlock(x, y - 1, z);

                boolean isSoil = ground == KCBlocks.seedGrass;
                if (isSoil && y < 256 - height - 1)
                {
                    ground.onPlantGrow(world, x, y - 1, z, x, y, z);

                    for (yPos = 0; yPos < height; ++yPos)
                    {
                        if (isReplaceable(world, x, y + yPos, z))
                        {
                            this.setBlockAndNotifyAdequately(world, x, y + yPos, z, Blocks.log, 0);
                        }
                    }

                    int counter = 0;
                    int leafWidth = 0;
                    int maxY = y + yPos + 2;
                    int minY = y + (int) (height / 1.5);

                    for (int leafY = maxY; leafY >= minY; leafY--)
                    {
                        int maxX = x + leafWidth;
                        int minX = x - leafWidth;

                        for (int leafX = maxX; leafX >= minX; leafX--)
                        {
                            int maxZ = z + leafWidth;
                            int minZ = z - leafWidth;

                            for (int leafZ = maxZ; leafZ >= minZ; leafZ--)
                            {
                                boolean isWoodHere = (leafX == x && leafZ == z && leafY < y + height);

                                if (((rand.nextInt(3) == 0 && y != minY || counter % 2 == 1) || leafWidth == 0 || (!checkCorners(leafX, leafZ, minX, minZ, maxX, maxZ)))
                                        && !isWoodHere && isReplaceable(world, leafX, leafY, leafZ))
                                {
                                    createLeaf(world, leafX, leafY, leafZ);
                                }
                            }
                        }
                        leafWidth += counter == 0 ? 1 : counter % 2;
                        counter++;
                    }
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    private void createLeaf(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z) == null || world.getBlock(x, y, z).getMaterial() != Material.wood)
        {
            if (world.rand.nextInt(10) == 0)
            {
                this.setBlockAndNotifyAdequately(world, x, y, z, KCBlocks.fruityLeaves, 0);
                ((IKCPlant) world.getBlock(x, y, z)).setFood(FoodType.getStackFor(type), world, x, y, z);
                world.markBlockForUpdate(x, y, z);
            }
            else
            {
                this.setBlockAndNotifyAdequately(world, x, y, z, KCBlocks.leaves, 0);
            }
        }
    }

    private boolean checkCorners(int leafX, int leafZ, int maxX, int maxZ, int minX, int minZ)
    {
        return (leafX == maxX && leafZ == maxZ) || (leafX == minX && leafZ == maxZ) || (leafX == maxX && leafZ == minZ) || (leafX == minX && leafZ == minZ);
    }

    @Override
    protected boolean isReplaceable(World world, int x, int y, int z)
    {
        return world.getBlock(x, y, z) == KCBlocks.sapling || super.isReplaceable(world, x, y, z);
    }
}
