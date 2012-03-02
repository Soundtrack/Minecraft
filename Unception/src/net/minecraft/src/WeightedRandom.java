package net.minecraft.src;

import java.util.*;

public class WeightedRandom
{
    public WeightedRandom()
    {
    }

    public static int getTotalWeight(Collection collection)
    {
        int i = 0;
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            WeightedRandomChoice weightedrandomchoice = (WeightedRandomChoice)iterator.next();
            i += weightedrandomchoice.itemWeight;
        }

        return i;
    }

    public static WeightedRandomChoice getRandomItem(Random random, Collection collection, int i)
    {
        if (i <= 0)
        {
            throw new IllegalArgumentException();
        }
        int j = random.nextInt(i);
        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            WeightedRandomChoice weightedrandomchoice = (WeightedRandomChoice)iterator.next();
            j -= weightedrandomchoice.itemWeight;
            if (j < 0)
            {
                return weightedrandomchoice;
            }
        }

        return null;
    }

    public static WeightedRandomChoice getRandomItem(Random random, Collection collection)
    {
        return getRandomItem(random, collection, getTotalWeight(collection));
    }

    public static int getTotalWeight(WeightedRandomChoice aweightedrandomchoice[])
    {
        int i = 0;
        WeightedRandomChoice aweightedrandomchoice1[] = aweightedrandomchoice;
        int j = aweightedrandomchoice1.length;
        for (int k = 0; k < j; k++)
        {
            WeightedRandomChoice weightedrandomchoice = aweightedrandomchoice1[k];
            i += weightedrandomchoice.itemWeight;
        }

        return i;
    }

    public static WeightedRandomChoice getRandomItem(Random random, WeightedRandomChoice aweightedrandomchoice[], int i)
    {
        if (i <= 0)
        {
            throw new IllegalArgumentException();
        }
        int j = random.nextInt(i);
        WeightedRandomChoice aweightedrandomchoice1[] = aweightedrandomchoice;
        int k = aweightedrandomchoice1.length;
        for (int l = 0; l < k; l++)
        {
            WeightedRandomChoice weightedrandomchoice = aweightedrandomchoice1[l];
            j -= weightedrandomchoice.itemWeight;
            if (j < 0)
            {
                return weightedrandomchoice;
            }
        }

        return null;
    }

    public static WeightedRandomChoice getRandomItem(Random random, WeightedRandomChoice aweightedrandomchoice[])
    {
        return getRandomItem(random, aweightedrandomchoice, getTotalWeight(aweightedrandomchoice));
    }
}
