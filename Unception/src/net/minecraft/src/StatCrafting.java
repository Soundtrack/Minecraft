package net.minecraft.src;

public class StatCrafting extends StatBase
{
    private final int itemID;

    public StatCrafting(int i, String s, int j)
    {
        super(i, s);
        itemID = j;
    }

    public int func_25072_b()
    {
        return itemID;
    }
}
