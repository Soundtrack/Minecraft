package net.minecraft.src;

public final class WorldSettings
{
    private final long seed;
    private final int gameType;
    private final boolean mapFeaturesEnabled;
    private final boolean hardcoreEnabled;
    private final EnumWorldType terrainType;

    public WorldSettings(long l, int i, boolean flag, boolean flag1, EnumWorldType enumworldtype)
    {
        seed = l;
        gameType = i;
        mapFeaturesEnabled = flag;
        hardcoreEnabled = flag1;
        terrainType = enumworldtype;
    }

    public long getSeed()
    {
        return seed;
    }

    public int getGameType()
    {
        return gameType;
    }

    public boolean getHardcoreEnabled()
    {
        return hardcoreEnabled;
    }

    public boolean isMapFeaturesEnabled()
    {
        return mapFeaturesEnabled;
    }

    public EnumWorldType getTerrainType()
    {
        return terrainType;
    }
}
