package net.minecraft.src;

import java.io.*;

public class Packet15Place extends Packet
{
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int direction;
    public ItemStack itemStack;

    public Packet15Place()
    {
    }

    public Packet15Place(int i, int j, int k, int l, ItemStack itemstack)
    {
        xPosition = i;
        yPosition = j;
        zPosition = k;
        direction = l;
        itemStack = itemstack;
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.read();
        zPosition = datainputstream.readInt();
        direction = datainputstream.read();
        itemStack = readItemStack(datainputstream);
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.write(yPosition);
        dataoutputstream.writeInt(zPosition);
        dataoutputstream.write(direction);
        writeItemStack(itemStack, dataoutputstream);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handlePlace(this);
    }

    public int getPacketSize()
    {
        return 15;
    }
}
