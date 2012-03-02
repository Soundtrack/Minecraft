package net.minecraft.src;

public class ChunkCoordIntPair
{
    public final int chunkXPos;
    public final int chunkZPos;

    public ChunkCoordIntPair(int i, int j)
    {
        chunkXPos = i;
        chunkZPos = j;
    }

    public static long chunkXZ2Int(int i, int j)
    {
        long l = i;
        long l1 = j;
        return l & 0xffffffffL | (l1 & 0xffffffffL) << 32;
    }

    public int hashCode()
    {
        long l = chunkXZ2Int(chunkXPos, chunkZPos);
        int i = (int)l;
        int j = (int)(l >> 32);
        return i ^ j;
    }

    public boolean equals(Object obj)
    {
        ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)obj;
        return chunkcoordintpair.chunkXPos == chunkXPos && chunkcoordintpair.chunkZPos == chunkZPos;
    }

    public int getCenterXPos()
    {
        return (chunkXPos << 4) + 8;
    }

    public int getCenterZPos()
    {
        return (chunkZPos << 4) + 8;
    }

    public ChunkPosition getChunkPosition(int i)
    {
        return new ChunkPosition(getCenterXPos(), i, getCenterZPos());
    }

    public String toString()
    {
        return (new StringBuilder()).append("[").append(chunkXPos).append(", ").append(chunkZPos).append("]").toString();
    }
}
