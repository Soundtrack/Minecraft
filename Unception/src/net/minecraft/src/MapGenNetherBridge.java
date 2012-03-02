package net.minecraft.src;

import java.util.*;

public class MapGenNetherBridge extends MapGenStructure
{
    private List spawnList;

    public MapGenNetherBridge()
    {
        spawnList = new ArrayList();
        spawnList.add(new SpawnListEntry(net.minecraft.src.EntityBlaze.class, 10, 2, 3));
        spawnList.add(new SpawnListEntry(net.minecraft.src.EntityPigZombie.class, 10, 4, 4));
        spawnList.add(new SpawnListEntry(net.minecraft.src.EntityMagmaCube.class, 3, 4, 4));
    }

    public List getSpawnList()
    {
        return spawnList;
    }

    protected boolean canSpawnStructureAtCoords(int i, int j)
    {
        int k = i >> 4;
        int l = j >> 4;
        rand.setSeed((long)(k ^ l << 4) ^ worldObj.getSeed());
        rand.nextInt();
        if (rand.nextInt(3) != 0)
        {
            return false;
        }
        if (i != (k << 4) + 4 + rand.nextInt(8))
        {
            return false;
        }
        return j == (l << 4) + 4 + rand.nextInt(8);
    }

    protected StructureStart getStructureStart(int i, int j)
    {
        return new StructureNetherBridgeStart(worldObj, rand, i, j);
    }
}
