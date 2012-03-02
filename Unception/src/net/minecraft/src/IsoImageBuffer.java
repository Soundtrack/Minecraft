package net.minecraft.src;

import java.awt.image.BufferedImage;

public class IsoImageBuffer
{
    public BufferedImage image;
    public World level;
    public int x;
    public int y;
    public boolean rendered;
    public boolean noContent;
    public int lastVisible;
    public boolean addedToRenderQueue;

    public IsoImageBuffer(World world, int i, int j)
    {
        rendered = false;
        noContent = false;
        lastVisible = 0;
        addedToRenderQueue = false;
        level = world;
        init(i, j);
    }

    public void init(int i, int j)
    {
        rendered = false;
        x = i;
        y = j;
        lastVisible = 0;
        addedToRenderQueue = false;
    }

    public void init(World world, int i, int j)
    {
        level = world;
        init(i, j);
    }
}
