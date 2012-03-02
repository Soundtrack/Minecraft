package net.minecraft.src;

import java.util.Random;

public class BlockLeaves extends BlockLeavesBase
{
    private int baseIndexInPNG;
    int adjacentTreeBlocks[];

    protected BlockLeaves(int i, int j)
    {
        super(i, j, Material.leaves, false);
        baseIndexInPNG = j;
        setTickOnLoad(true);
    }

    public int getBlockColor()
    {
        double d = 0.5D;
        double d1 = 1.0D;
        return ColorizerFoliage.getFoliageColor(d, d1);
    }

    public int getRenderColor(int i)
    {
        if ((i & 1) == 1)
        {
            return ColorizerFoliage.getFoliageColorPine();
        }
        if ((i & 2) == 2)
        {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        else
        {
            return ColorizerFoliage.getFoliageColorBasic();
        }
    }

    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k);
        if ((l & 1) == 1)
        {
            return ColorizerFoliage.getFoliageColorPine();
        }
        if ((l & 2) == 2)
        {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        for (int l1 = -1; l1 <= 1; l1++)
        {
            for (int i2 = -1; i2 <= 1; i2++)
            {
                int j2 = iblockaccess.getWorldChunkManager().getBiomeGenAt(i + i2, k + l1).getFoliageColorAtCoords(iblockaccess, i + i2, j, k + l1);
                i1 += (j2 & 0xff0000) >> 16;
                j1 += (j2 & 0xff00) >> 8;
                k1 += j2 & 0xff;
            }
        }

        return (i1 / 9 & 0xff) << 16 | (j1 / 9 & 0xff) << 8 | k1 / 9 & 0xff;
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        int l = 1;
        int i1 = l + 1;
        if (world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1))
        {
            for (int j1 = -l; j1 <= l; j1++)
            {
                for (int k1 = -l; k1 <= l; k1++)
                {
                    for (int l1 = -l; l1 <= l; l1++)
                    {
                        int i2 = world.getBlockId(i + j1, j + k1, k + l1);
                        if (i2 == Block.leaves.blockID)
                        {
                            int j2 = world.getBlockMetadata(i + j1, j + k1, k + l1);
                            world.setBlockMetadata(i + j1, j + k1, k + l1, j2 | 8);
                        }
                    }
                }
            }
        }
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (world.isRemote)
        {
            return;
        }
        int l = world.getBlockMetadata(i, j, k);
        if ((l & 8) != 0 && (l & 4) == 0)
        {
            byte byte0 = 4;
            int i1 = byte0 + 1;
            byte byte1 = 32;
            int j1 = byte1 * byte1;
            int k1 = byte1 / 2;
            if (adjacentTreeBlocks == null)
            {
                adjacentTreeBlocks = new int[byte1 * byte1 * byte1];
            }
            if (world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1))
            {
                for (int l1 = -byte0; l1 <= byte0; l1++)
                {
                    for (int k2 = -byte0; k2 <= byte0; k2++)
                    {
                        for (int i3 = -byte0; i3 <= byte0; i3++)
                        {
                            int k3 = world.getBlockId(i + l1, j + k2, k + i3);
                            if (k3 == Block.wood.blockID)
                            {
                                adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = 0;
                                continue;
                            }
                            if (k3 == Block.leaves.blockID)
                            {
                                adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -2;
                            }
                            else
                            {
                                adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -1;
                            }
                        }
                    }
                }

                for (int i2 = 1; i2 <= 4; i2++)
                {
                    for (int l2 = -byte0; l2 <= byte0; l2++)
                    {
                        for (int j3 = -byte0; j3 <= byte0; j3++)
                        {
                            for (int l3 = -byte0; l3 <= byte0; l3++)
                            {
                                if (adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] != i2 - 1)
                                {
                                    continue;
                                }
                                if (adjacentTreeBlocks[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
                                }
                                if (adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
                                }
                                if (adjacentTreeBlocks[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] = i2;
                                }
                                if (adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] = i2;
                                }
                                if (adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] = i2;
                                }
                                if (adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] == -2)
                                {
                                    adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] = i2;
                                }
                            }
                        }
                    }
                }
            }
            int j2 = adjacentTreeBlocks[k1 * j1 + k1 * byte1 + k1];
            if (j2 >= 0)
            {
                world.setBlockMetadata(i, j, k, l & -9);
            }
            else
            {
                removeLeaves(world, i, j, k);
            }
        }
    }

    private void removeLeaves(World world, int i, int j, int k)
    {
        dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
        world.setBlockWithNotify(i, j, k, 0);
    }

    public int quantityDropped(Random random)
    {
        return random.nextInt(20) != 0 ? 0 : 1;
    }

    public int idDropped(int i, Random random, int j)
    {
        return Block.sapling.blockID;
    }

    public void dropBlockAsItemWithChance(World world, int i, int j, int k, int l, float f, int i1)
    {
        super.dropBlockAsItemWithChance(world, i, j, k, l, f, i1);
        if (!world.isRemote && (l & 3) == 0 && world.rand.nextInt(200) == 0)
        {
            dropBlockAsItem_do(world, i, j, k, new ItemStack(Item.appleRed, 1, 0));
        }
    }

    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
    {
        if (!world.isRemote && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex)
        {
            entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
            dropBlockAsItem_do(world, i, j, k, new ItemStack(Block.leaves.blockID, 1, l & 3));
        }
        else
        {
            super.harvestBlock(world, entityplayer, i, j, k, l);
        }
    }

    protected int damageDropped(int i)
    {
        return i & 3;
    }

    public boolean isOpaqueCube()
    {
        return !graphicsLevel;
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if ((j & 3) == 1)
        {
            return blockIndexInTexture + 80;
        }
        else
        {
            return blockIndexInTexture;
        }
    }

    public void setGraphicsLevel(boolean flag)
    {
        graphicsLevel = flag;
        blockIndexInTexture = baseIndexInPNG + (flag ? 0 : 1);
    }

    public void onEntityWalking(World world, int i, int j, int k, Entity entity)
    {
        super.onEntityWalking(world, i, j, k, entity);
    }
}
