package net.minecraft.src;

public class ItemMetadata extends ItemBlock
{
    private Block blockObj;

    public ItemMetadata(int i, Block block)
    {
        super(i);
        blockObj = block;
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getIconFromDamage(int i)
    {
        return blockObj.getBlockTextureFromSideAndMetadata(2, i);
    }

    public int getMetadata(int i)
    {
        return i;
    }
}
