package net.minecraft.src;

public abstract class WorldProvider
{
    public World worldObj;
    public EnumWorldType terrainType;
    public WorldChunkManager worldChunkMgr;
    public boolean isAlternateDimension;
    public boolean isHellWorld;
    public boolean hasNoSky;
    public float lightBrightnessTable[];
    public int worldType;
    private float colorsSunriseSunset[];

    public WorldProvider()
    {
        isAlternateDimension = false;
        isHellWorld = false;
        hasNoSky = false;
        lightBrightnessTable = new float[16];
        worldType = 0;
        colorsSunriseSunset = new float[4];
    }

    public final void registerWorld(World world)
    {
        worldObj = world;
        terrainType = world.getWorldInfo().getTerrainType();
        registerWorldChunkManager();
        generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable()
    {
        float f = 0.0F;
        for (int i = 0; i <= 15; i++)
        {
            float f1 = 1.0F - (float)i / 15F;
            lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F)) * (1.0F - f) + f;
        }
    }

    protected void registerWorldChunkManager()
    {
        if (worldObj.getWorldInfo().getTerrainType() == EnumWorldType.FLAT)
        {
            worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F, 0.5F);
        }
        else
        {
            worldChunkMgr = new WorldChunkManager(worldObj);
        }
    }

    public IChunkProvider getChunkProvider()
    {
        if (terrainType == EnumWorldType.FLAT)
        {
            return new ChunkProviderFlat(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
        }
        else
        {
            return new ChunkProviderGenerate(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
        }
    }

    public boolean canCoordinateBeSpawn(int i, int j)
    {
        int k = worldObj.getFirstUncoveredBlock(i, j);
        return k == Block.grass.blockID;
    }

    public float calculateCelestialAngle(long l, float f)
    {
        int i = (int)(l % 24000L);
        float f1 = ((float)i + f) / 24000F - 0.25F;
        if (f1 < 0.0F)
        {
            f1++;
        }
        if (f1 > 1.0F)
        {
            f1--;
        }
        float f2 = f1;
        f1 = 1.0F - (float)((Math.cos((double)f1 * 3.1415926535897931D) + 1.0D) / 2D);
        f1 = f2 + (f1 - f2) / 3F;
        return f1;
    }

    public int func_40470_b(long l, float f)
    {
        return (int)(l / 24000L) % 8;
    }

    public float[] calcSunriseSunsetColors(float f, float f1)
    {
        float f2 = 0.4F;
        float f3 = MathHelper.cos(f * 3.141593F * 2.0F) - 0.0F;
        float f4 = -0F;
        if (f3 >= f4 - f2 && f3 <= f4 + f2)
        {
            float f5 = ((f3 - f4) / f2) * 0.5F + 0.5F;
            float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * 3.141593F)) * 0.99F;
            f6 *= f6;
            colorsSunriseSunset[0] = f5 * 0.3F + 0.7F;
            colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
            colorsSunriseSunset[2] = f5 * f5 * 0.0F + 0.2F;
            colorsSunriseSunset[3] = f6;
            return colorsSunriseSunset;
        }
        else
        {
            return null;
        }
    }

    public Vec3D getFogColor(float f, float f1)
    {
        float f2 = MathHelper.cos(f * 3.141593F * 2.0F) * 2.0F + 0.5F;
        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }
        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }
        float f3 = 0.7529412F;
        float f4 = 0.8470588F;
        float f5 = 1.0F;
        f3 *= f2 * 0.94F + 0.06F;
        f4 *= f2 * 0.94F + 0.06F;
        f5 *= f2 * 0.91F + 0.09F;
        return Vec3D.createVector(f3, f4, f5);
    }

    public boolean canRespawnHere()
    {
        return true;
    }

    public static WorldProvider getProviderForDimension(int i)
    {
        if (i == -1)
        {
            return new WorldProviderHell();
        }
        if (i == 0)
        {
            return new WorldProviderSurface();
        }
        if (i == 1)
        {
            return new WorldProviderEnd();
        }
        else
        {
            return null;
        }
    }

    public float getCloudHeight()
    {
        return (float)worldObj.worldHeight;
    }

    public boolean func_28112_c()
    {
        return true;
    }

    public ChunkCoordinates getEntrancePortalLocation()
    {
        return null;
    }

    public int getAverageGroundLevel()
    {
        if (terrainType == EnumWorldType.FLAT)
        {
            return 4;
        }
        else
        {
            return worldObj.worldHeight / 2;
        }
    }

    public boolean getWorldHasNoSky()
    {
        return terrainType != EnumWorldType.FLAT && !hasNoSky;
    }

    public double func_46065_j()
    {
        return terrainType != EnumWorldType.FLAT ? 0.03125D : 1.0D;
    }
}
