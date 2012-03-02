package net.minecraft.src;

class ThreadedChunkLoaderPending
{
    public final ChunkCoordIntPair chunkPosition;
    public final NBTTagCompound chunkData;

    public ThreadedChunkLoaderPending(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound)
    {
        chunkPosition = chunkcoordintpair;
        chunkData = nbttagcompound;
    }
}
