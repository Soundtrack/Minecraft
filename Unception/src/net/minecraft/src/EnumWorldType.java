package net.minecraft.src;

public enum EnumWorldType
{
    DEFAULT("DEFAULT", 0, "default"),
    FLAT("FLAT", 1, "flat");

    private String worldType;
    private static final EnumWorldType allWorldTypes[] = (new EnumWorldType[] {
        DEFAULT, FLAT
    });

    private EnumWorldType(String s, int i, String s1)
    {
        worldType = s1;
    }

    public String func_46136_a()
    {
        return (new StringBuilder()).append("generator.").append(worldType).toString();
    }

    public static EnumWorldType parseWorldType(String s)
    {
        EnumWorldType aenumworldtype[] = values();
        int i = aenumworldtype.length;
        for (int j = 0; j < i; j++)
        {
            EnumWorldType enumworldtype = aenumworldtype[j];
            if (enumworldtype.name().equals(s))
            {
                return enumworldtype;
            }
        }

        return null;
    }
}
