package net.minecraft.src;

import java.util.*;

class GuiSlotStatsItem extends GuiSlotStats
{
    final GuiStats field_27275_a;

    public GuiSlotStatsItem(GuiStats guistats)
    {
        super(guistats);
        field_27275_a = guistats;
        field_27273_c = new ArrayList();
        Iterator iterator = StatList.itemStats.iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            StatCrafting statcrafting = (StatCrafting)iterator.next();
            boolean flag = false;
            int i = statcrafting.func_25072_b();
            if (GuiStats.getStatsFileWriter(guistats).writeStat(statcrafting) > 0)
            {
                flag = true;
            }
            else if (StatList.objectBreakStats[i] != null && GuiStats.getStatsFileWriter(guistats).writeStat(StatList.objectBreakStats[i]) > 0)
            {
                flag = true;
            }
            else if (StatList.objectCraftStats[i] != null && GuiStats.getStatsFileWriter(guistats).writeStat(StatList.objectCraftStats[i]) > 0)
            {
                flag = true;
            }
            if (flag)
            {
                field_27273_c.add(statcrafting);
            }
        }
        while (true);
        field_27272_d = new SorterStatsItem(this, guistats);
    }

    protected void func_27260_a(int i, int j, Tessellator tessellator)
    {
        super.func_27260_a(i, j, tessellator);
        if (field_27268_b == 0)
        {
            GuiStats.drawSprite(field_27275_a, ((i + 115) - 18) + 1, j + 1 + 1, 72, 18);
        }
        else
        {
            GuiStats.drawSprite(field_27275_a, (i + 115) - 18, j + 1, 72, 18);
        }
        if (field_27268_b == 1)
        {
            GuiStats.drawSprite(field_27275_a, ((i + 165) - 18) + 1, j + 1 + 1, 18, 18);
        }
        else
        {
            GuiStats.drawSprite(field_27275_a, (i + 165) - 18, j + 1, 18, 18);
        }
        if (field_27268_b == 2)
        {
            GuiStats.drawSprite(field_27275_a, ((i + 215) - 18) + 1, j + 1 + 1, 36, 18);
        }
        else
        {
            GuiStats.drawSprite(field_27275_a, (i + 215) - 18, j + 1, 36, 18);
        }
    }

    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator)
    {
        StatCrafting statcrafting = func_27264_b(i);
        int i1 = statcrafting.func_25072_b();
        GuiStats.drawItemSprite(field_27275_a, j + 40, k, i1);
        func_27265_a((StatCrafting)StatList.objectBreakStats[i1], j + 115, k, i % 2 == 0);
        func_27265_a((StatCrafting)StatList.objectCraftStats[i1], j + 165, k, i % 2 == 0);
        func_27265_a(statcrafting, j + 215, k, i % 2 == 0);
    }

    protected String func_27263_a(int i)
    {
        if (i == 1)
        {
            return "stat.crafted";
        }
        if (i == 2)
        {
            return "stat.used";
        }
        else
        {
            return "stat.depleted";
        }
    }
}
