package net.minecraft.src;

import java.util.Random;

public abstract class WorldGenerator
{
    private final boolean doBlockNotify;

    public WorldGenerator()
    {
        doBlockNotify = false;
    }

    public WorldGenerator(boolean flag)
    {
        doBlockNotify = flag;
    }

    public abstract boolean generate(World world, Random random, int i, int j, int k);

    public void setScale(double d, double d1, double d2)
    {
    }

    protected void setBlockAndMetadata(World world, int i, int j, int k, int l, int i1)
    {
        if (doBlockNotify)
        {
            world.setBlockAndMetadataWithNotify(i, j, k, l, i1);
        }
        else
        {
            world.setBlockAndMetadata(i, j, k, l, i1);
        }
    }
}
