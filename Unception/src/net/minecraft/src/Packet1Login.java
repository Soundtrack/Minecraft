package net.minecraft.src;

import java.io.*;

public class Packet1Login extends Packet
{
    public int protocolVersion;
    public String username;
    public long mapSeed;
    public EnumWorldType terrainType;
    public int serverMode;
    public byte worldType;
    public byte difficultySetting;
    public byte worldHeight;
    public byte maxPlayers;

    public Packet1Login()
    {
    }

    public Packet1Login(String s, int i)
    {
        username = s;
        protocolVersion = i;
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        protocolVersion = datainputstream.readInt();
        username = readString(datainputstream, 16);
        mapSeed = datainputstream.readLong();
        String s = readString(datainputstream, 16);
        terrainType = EnumWorldType.parseWorldType(s);
        if (terrainType == null)
        {
            terrainType = EnumWorldType.DEFAULT;
        }
        serverMode = datainputstream.readInt();
        worldType = datainputstream.readByte();
        difficultySetting = datainputstream.readByte();
        worldHeight = datainputstream.readByte();
        maxPlayers = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        dataoutputstream.writeInt(protocolVersion);
        writeString(username, dataoutputstream);
        dataoutputstream.writeLong(mapSeed);
        if (terrainType == null)
        {
            writeString("", dataoutputstream);
        }
        else
        {
            writeString(terrainType.name(), dataoutputstream);
        }
        dataoutputstream.writeInt(serverMode);
        dataoutputstream.writeByte(worldType);
        dataoutputstream.writeByte(difficultySetting);
        dataoutputstream.writeByte(worldHeight);
        dataoutputstream.writeByte(maxPlayers);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleLogin(this);
    }

    public int getPacketSize()
    {
        int i = 0;
        if (terrainType != null)
        {
            i = terrainType.name().length();
        }
        return 4 + username.length() + 4 + 7 + 4 + i;
    }
}
