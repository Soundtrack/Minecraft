package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockPistonExtension extends Block
{
    private int field_31053_a;

    public BlockPistonExtension(int i, int j)
    {
        super(i, j, Material.piston);
        field_31053_a = -1;
        setStepSound(soundStoneFootstep);
        setHardness(0.5F);
    }

    public void func_31052_a_(int i)
    {
        field_31053_a = i;
    }

    public void func_31051_a()
    {
        field_31053_a = -1;
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        super.onBlockRemoval(world, i, j, k);
        int l = world.getBlockMetadata(i, j, k);
        int j1 = Facing.field_31057_a[func_31050_c(l)];
        i += Facing.offsetsXForSide[j1];
        j += Facing.offsetsYForSide[j1];
        k += Facing.offsetsZForSide[j1];
        int k1 = world.getBlockId(i, j, k);
        if (k1 == Block.pistonBase.blockID || k1 == Block.pistonStickyBase.blockID)
        {
            int i1 = world.getBlockMetadata(i, j, k);
            if (BlockPistonBase.isExtended(i1))
            {
                Block.blocksList[k1].dropBlockAsItem(world, i, j, k, i1, 0);
                world.setBlockWithNotify(i, j, k, 0);
            }
        }
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        int k = func_31050_c(j);
        if (i == k)
        {
            if (field_31053_a >= 0)
            {
                return field_31053_a;
            }
            if ((j & 8) != 0)
            {
                return blockIndexInTexture - 1;
            }
            else
            {
                return blockIndexInTexture;
            }
        }
        return i != Facing.field_31057_a[k] ? 108 : 107;
    }

    public int getRenderType()
    {
        return 17;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return false;
    }

    public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l)
    {
        return false;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist)
    {
        int l = world.getBlockMetadata(i, j, k);
        switch (func_31050_c(l))
        {
            case 0:
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                setBlockBounds(0.375F, 0.25F, 0.375F, 0.625F, 1.0F, 0.625F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                break;

            case 1:
                setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 0.75F, 0.625F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                break;

            case 2:
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                setBlockBounds(0.25F, 0.375F, 0.25F, 0.75F, 0.625F, 1.0F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                break;

            case 3:
                setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                setBlockBounds(0.25F, 0.375F, 0.0F, 0.75F, 0.625F, 0.75F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                break;

            case 4:
                setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                setBlockBounds(0.375F, 0.25F, 0.25F, 0.625F, 0.75F, 1.0F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                break;

            case 5:
                setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                setBlockBounds(0.0F, 0.375F, 0.25F, 0.75F, 0.625F, 0.75F);
                super.getCollidingBoundingBoxes(world, i, j, k, axisalignedbb, arraylist);
                break;
        }
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        int l = iblockaccess.getBlockMetadata(i, j, k);
        switch (func_31050_c(l))
        {
            case 0:
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                break;

            case 1:
                setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;

            case 2:
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                break;

            case 3:
                setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                break;

            case 4:
                setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                break;

            case 5:
                setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        int i1 = func_31050_c(world.getBlockMetadata(i, j, k));
        int j1 = world.getBlockId(i - Facing.offsetsXForSide[i1], j - Facing.offsetsYForSide[i1], k - Facing.offsetsZForSide[i1]);
        if (j1 != Block.pistonBase.blockID && j1 != Block.pistonStickyBase.blockID)
        {
            world.setBlockWithNotify(i, j, k, 0);
        }
        else
        {
            Block.blocksList[j1].onNeighborBlockChange(world, i - Facing.offsetsXForSide[i1], j - Facing.offsetsYForSide[i1], k - Facing.offsetsZForSide[i1], l);
        }
    }

    public static int func_31050_c(int i)
    {
        return i & 7;
    }
}
