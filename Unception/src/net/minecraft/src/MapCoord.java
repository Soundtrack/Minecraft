package net.minecraft.src;

public class MapCoord
{
    public byte field_28217_a;
    public byte centerX;
    public byte centerZ;
    public byte iconRotation;
    final MapData data;

    public MapCoord(MapData mapdata, byte byte0, byte byte1, byte byte2, byte byte3)
    {
        data = mapdata;

        field_28217_a = byte0;
        centerX = byte1;
        centerZ = byte2;
        iconRotation = byte3;
    }
}
