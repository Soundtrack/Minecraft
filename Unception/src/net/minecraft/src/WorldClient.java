package net.minecraft.src;

import java.util.*;

public class WorldClient extends World
{
    private LinkedList blocksToReceive;
    private NetClientHandler sendQueue;
    private ChunkProviderClient field_20915_C;
    private IntHashMap entityHashSet;
    private Set entityList;
    private Set entitySpawnQueue;

    public WorldClient(NetClientHandler netclienthandler, WorldSettings worldsettings, int i, int j)
    {
        super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(i), worldsettings);
        blocksToReceive = new LinkedList();
        entityHashSet = new IntHashMap();
        entityList = new HashSet();
        entitySpawnQueue = new HashSet();
        sendQueue = netclienthandler;
        difficultySetting = j;
        setSpawnPoint(new ChunkCoordinates(8, 64, 8));
        mapStorage = netclienthandler.mapStorage;
    }

    public void tick()
    {
        setWorldTime(getWorldTime() + 1L);
        for (int i = 0; i < 10 && !entitySpawnQueue.isEmpty(); i++)
        {
            Entity entity = (Entity)entitySpawnQueue.iterator().next();
            entitySpawnQueue.remove(entity);
            if (!loadedEntityList.contains(entity))
            {
                spawnEntityInWorld(entity);
            }
        }

        sendQueue.processReadPackets();
        for (int j = 0; j < blocksToReceive.size(); j++)
        {
            WorldBlockPositionType worldblockpositiontype = (WorldBlockPositionType)blocksToReceive.get(j);
            if (--worldblockpositiontype.acceptCountdown == 0)
            {
                super.setBlockAndMetadata(worldblockpositiontype.posX, worldblockpositiontype.posY, worldblockpositiontype.posZ, worldblockpositiontype.blockID, worldblockpositiontype.metadata);
                super.markBlockNeedsUpdate(worldblockpositiontype.posX, worldblockpositiontype.posY, worldblockpositiontype.posZ);
                blocksToReceive.remove(j--);
            }
        }
    }

    public void invalidateBlockReceiveRegion(int i, int j, int k, int l, int i1, int j1)
    {
        for (int k1 = 0; k1 < blocksToReceive.size(); k1++)
        {
            WorldBlockPositionType worldblockpositiontype = (WorldBlockPositionType)blocksToReceive.get(k1);
            if (worldblockpositiontype.posX >= i && worldblockpositiontype.posY >= j && worldblockpositiontype.posZ >= k && worldblockpositiontype.posX <= l && worldblockpositiontype.posY <= i1 && worldblockpositiontype.posZ <= j1)
            {
                blocksToReceive.remove(k1--);
            }
        }
    }

    protected IChunkProvider getChunkProvider()
    {
        field_20915_C = new ChunkProviderClient(this);
        return field_20915_C;
    }

    public void setSpawnLocation()
    {
        setSpawnPoint(new ChunkCoordinates(8, 64, 8));
    }

    protected void updateBlocksAndPlayCaveSounds()
    {
    }

    public void scheduleBlockUpdate(int i, int j, int k, int l, int i1)
    {
    }

    public boolean tickUpdates(boolean flag)
    {
        return false;
    }

    public void doPreChunk(int i, int j, boolean flag)
    {
        if (flag)
        {
            field_20915_C.loadChunk(i, j);
        }
        else
        {
            field_20915_C.func_539_c(i, j);
        }
        if (!flag)
        {
            markBlocksDirty(i * 16, 0, j * 16, i * 16 + 15, worldHeight, j * 16 + 15);
        }
    }

    public boolean spawnEntityInWorld(Entity entity)
    {
        boolean flag = super.spawnEntityInWorld(entity);
        entityList.add(entity);
        if (!flag)
        {
            entitySpawnQueue.add(entity);
        }
        return flag;
    }

    public void setEntityDead(Entity entity)
    {
        super.setEntityDead(entity);
        entityList.remove(entity);
    }

    protected void obtainEntitySkin(Entity entity)
    {
        super.obtainEntitySkin(entity);
        if (entitySpawnQueue.contains(entity))
        {
            entitySpawnQueue.remove(entity);
        }
    }

    protected void releaseEntitySkin(Entity entity)
    {
        super.releaseEntitySkin(entity);
        if (entityList.contains(entity))
        {
            entitySpawnQueue.add(entity);
        }
    }

    public void addEntityToWorld(int i, Entity entity)
    {
        Entity entity1 = getEntityByID(i);
        if (entity1 != null)
        {
            setEntityDead(entity1);
        }
        entityList.add(entity);
        entity.entityId = i;
        if (!spawnEntityInWorld(entity))
        {
            entitySpawnQueue.add(entity);
        }
        entityHashSet.addKey(i, entity);
    }

    public Entity getEntityByID(int i)
    {
        return (Entity)entityHashSet.lookup(i);
    }

    public Entity removeEntityFromWorld(int i)
    {
        Entity entity = (Entity)entityHashSet.removeObject(i);
        if (entity != null)
        {
            entityList.remove(entity);
            setEntityDead(entity);
        }
        return entity;
    }

    public boolean setBlockMetadata(int i, int j, int k, int l)
    {
        int i1 = getBlockId(i, j, k);
        int j1 = getBlockMetadata(i, j, k);
        if (super.setBlockMetadata(i, j, k, l))
        {
            blocksToReceive.add(new WorldBlockPositionType(this, i, j, k, i1, j1));
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean setBlockAndMetadata(int i, int j, int k, int l, int i1)
    {
        int j1 = getBlockId(i, j, k);
        int k1 = getBlockMetadata(i, j, k);
        if (super.setBlockAndMetadata(i, j, k, l, i1))
        {
            blocksToReceive.add(new WorldBlockPositionType(this, i, j, k, j1, k1));
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean setBlock(int i, int j, int k, int l)
    {
        int i1 = getBlockId(i, j, k);
        int j1 = getBlockMetadata(i, j, k);
        if (super.setBlock(i, j, k, l))
        {
            blocksToReceive.add(new WorldBlockPositionType(this, i, j, k, i1, j1));
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean setBlockAndMetadataAndInvalidate(int i, int j, int k, int l, int i1)
    {
        invalidateBlockReceiveRegion(i, j, k, i, j, k);
        if (super.setBlockAndMetadata(i, j, k, l, i1))
        {
            notifyBlockChange(i, j, k, l);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void sendQuittingDisconnectingPacket()
    {
        sendQueue.func_28117_a(new Packet255KickDisconnect("Quitting"));
    }

    protected void updateWeather()
    {
        if (worldProvider.hasNoSky)
        {
            return;
        }
        if (lastLightningBolt > 0)
        {
            lastLightningBolt--;
        }
        prevRainingStrength = rainingStrength;
        if (worldInfo.getIsRaining())
        {
            rainingStrength += 0.01D;
        }
        else
        {
            rainingStrength -= 0.01D;
        }
        if (rainingStrength < 0.0F)
        {
            rainingStrength = 0.0F;
        }
        if (rainingStrength > 1.0F)
        {
            rainingStrength = 1.0F;
        }
        prevThunderingStrength = thunderingStrength;
        if (worldInfo.getIsThundering())
        {
            thunderingStrength += 0.01D;
        }
        else
        {
            thunderingStrength -= 0.01D;
        }
        if (thunderingStrength < 0.0F)
        {
            thunderingStrength = 0.0F;
        }
        if (thunderingStrength > 1.0F)
        {
            thunderingStrength = 1.0F;
        }
    }
}
