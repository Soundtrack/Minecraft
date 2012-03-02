package net.minecraft.src;

import java.io.*;

public class Packet9Respawn extends Packet
{
    public long mapSeed;
    public int respawnDimension;
    public int difficulty;
    public int worldHeight;
    public int creativeMode;
    public EnumWorldType terrainType;

    public Packet9Respawn()
    {
    }

    public Packet9Respawn(byte byte0, byte byte1, long l, EnumWorldType enumworldtype, int i, int j)
    {
        respawnDimension = byte0;
        difficulty = byte1;
        mapSeed = l;
        worldHeight = i;
        creativeMode = j;
        terrainType = enumworldtype;
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleRespawn(this);
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        respawnDimension = datainputstream.readByte();
        difficulty = datainputstream.readByte();
        creativeMode = datainputstream.readByte();
        worldHeight = datainputstream.readShort();
        mapSeed = datainputstream.readLong();
        String s = readString(datainputstream, 16);
        terrainType = EnumWorldType.parseWorldType(s);
        if (terrainType == null)
        {
            terrainType = EnumWorldType.DEFAULT;
        }
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        dataoutputstream.writeByte(respawnDimension);
        dataoutputstream.writeByte(difficulty);
        dataoutputstream.writeByte(creativeMode);
        dataoutputstream.writeShort(worldHeight);
        dataoutputstream.writeLong(mapSeed);
        writeString(terrainType.name(), dataoutputstream);
    }

    public int getPacketSize()
    {
        return 13 + terrainType.name().length();
    }
}
