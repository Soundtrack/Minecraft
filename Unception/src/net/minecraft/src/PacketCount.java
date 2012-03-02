package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class PacketCount
{
    public static boolean allowCounting = true;
    private static final Map packetCountForID = new HashMap();
    private static final Map sizeCountForID = new HashMap();
    private static final Object lock = new Object();

    public PacketCount()
    {
    }

    public static void countPacket(int i, long l)
    {
        if (!allowCounting)
        {
            return;
        }
        synchronized (lock)
        {
            if (packetCountForID.containsKey(Integer.valueOf(i)))
            {
                packetCountForID.put(Integer.valueOf(i), Long.valueOf(((Long)packetCountForID.get(Integer.valueOf(i))).longValue() + 1L));
                sizeCountForID.put(Integer.valueOf(i), Long.valueOf(((Long)sizeCountForID.get(Integer.valueOf(i))).longValue() + l));
            }
            else
            {
                packetCountForID.put(Integer.valueOf(i), Long.valueOf(1L));
                sizeCountForID.put(Integer.valueOf(i), Long.valueOf(l));
            }
        }
    }
}
