package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ChunkProviderFlat
    implements IChunkProvider
{
    private World worldObj;
    private Random random;
    private final boolean useStructures;
    private MapGenVillage villageGen;

    public ChunkProviderFlat(World world, long l, boolean flag)
    {
        villageGen = new MapGenVillage(1);
        worldObj = world;
        useStructures = flag;
        random = new Random(l);
    }

    private void generate(byte abyte0[])
    {
        int i = abyte0.length / 256;
        for (int j = 0; j < 16; j++)
        {
            for (int k = 0; k < 16; k++)
            {
                for (int l = 0; l < i; l++)
                {
                    int i1 = 0;
                    if (l == 0)
                    {
                        i1 = Block.bedrock.blockID;
                    }
                    else if (l <= 2)
                    {
                        i1 = Block.dirt.blockID;
                    }
                    else if (l == 3)
                    {
                        i1 = Block.grass.blockID;
                    }
                    abyte0[j << 11 | k << 7 | l] = (byte)i1;
                }
            }
        }
    }

    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public Chunk provideChunk(int i, int j)
    {
        byte abyte0[] = new byte[16 * worldObj.worldHeight * 16];
        Chunk chunk = new Chunk(worldObj, abyte0, i, j);
        generate(abyte0);
        if (useStructures)
        {
            villageGen.generate(this, worldObj, i, j, abyte0);
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    public boolean chunkExists(int i, int j)
    {
        return true;
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        random.setSeed(worldObj.getSeed());
        long l = (random.nextLong() / 2L) * 2L + 1L;
        long l1 = (random.nextLong() / 2L) * 2L + 1L;
        random.setSeed((long)i * l + (long)j * l1 ^ worldObj.getSeed());
        if (useStructures)
        {
            villageGen.generateStructuresInChunk(worldObj, random, i, j);
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        return true;
    }

    public boolean unload100OldestChunks()
    {
        return false;
    }

    public boolean canSave()
    {
        return true;
    }

    public String makeString()
    {
        return "FlatLevelSource";
    }

    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        WorldChunkManager worldchunkmanager = worldObj.getWorldChunkManager();
        if (worldchunkmanager == null)
        {
            return null;
        }
        BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAtChunkCoord(new ChunkCoordIntPair(i >> 4, k >> 4));
        if (biomegenbase == null)
        {
            return null;
        }
        else
        {
            return biomegenbase.getSpawnableList(enumcreaturetype);
        }
    }

    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k)
    {
        return null;
    }
}
