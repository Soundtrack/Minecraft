package net.minecraft.src;

import java.io.*;

public class Packet250CustomPayload extends Packet
{
    public String channel;
    public int length;
    public byte data[];

    public Packet250CustomPayload()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        channel = readString(datainputstream, 16);
        length = datainputstream.readShort();
        if (length > 0 && length < 32767)
        {
            data = new byte[length];
            datainputstream.read(data);
        }
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        writeString(channel, dataoutputstream);
        dataoutputstream.writeShort((short)length);
        if (data != null)
        {
            dataoutputstream.write(data);
        }
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleCustomPayload(this);
    }

    public int getPacketSize()
    {
        return 2 + channel.length() * 2 + 2 + length;
    }
}
