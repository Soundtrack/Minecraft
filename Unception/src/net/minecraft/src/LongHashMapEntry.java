package net.minecraft.src;

class LongHashMapEntry
{
    final long key;
    Object value;
    LongHashMapEntry nextEntry;
    final int hash;

    LongHashMapEntry(int i, long l, Object obj, LongHashMapEntry longhashmapentry)
    {
        value = obj;
        nextEntry = longhashmapentry;
        key = l;
        hash = i;
    }

    public final long getKey()
    {
        return key;
    }

    public final Object getValue()
    {
        return value;
    }

    public final boolean equals(Object obj)
    {
        if (!(obj instanceof LongHashMapEntry))
        {
            return false;
        }
        LongHashMapEntry longhashmapentry = (LongHashMapEntry)obj;
        Long long1 = Long.valueOf(getKey());
        Long long2 = Long.valueOf(longhashmapentry.getKey());
        if (long1 == long2 || long1 != null && long1.equals(long2))
        {
            Object obj1 = getValue();
            Object obj2 = longhashmapentry.getValue();
            if (obj1 == obj2 || obj1 != null && obj1.equals(obj2))
            {
                return true;
            }
        }
        return false;
    }

    public final int hashCode()
    {
        return LongHashMap.getHashCode(key);
    }

    public final String toString()
    {
        return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
    }
}
