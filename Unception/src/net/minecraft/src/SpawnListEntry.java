package net.minecraft.src;

public class SpawnListEntry extends WeightedRandomChoice
{
    public Class entityClass;
    public int minGroupCount;
    public int maxGroupCount;

    public SpawnListEntry(Class class1, int i, int j, int k)
    {
        super(i);
        entityClass = class1;
        minGroupCount = j;
        maxGroupCount = k;
    }
}
