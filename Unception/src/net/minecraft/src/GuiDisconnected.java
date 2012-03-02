package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiDisconnected extends GuiScreen
{
    private String errorMessage;
    private String errorDetail;

    public GuiDisconnected(String s, String s1, Object aobj[])
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        errorMessage = stringtranslate.translateKey(s);
        if (aobj != null)
        {
            errorDetail = stringtranslate.translateKeyFormat(s1, aobj);
        }
        else
        {
            errorDetail = stringtranslate.translateKey(s1);
        }
    }

    public void updateScreen()
    {
    }

    protected void keyTyped(char c, int i)
    {
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.toMenu")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.id == 0)
        {
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, errorMessage, width / 2, height / 2 - 50, 0xffffff);
        drawCenteredString(fontRenderer, errorDetail, width / 2, height / 2 - 10, 0xffffff);
        super.drawScreen(i, j, f);
    }
}
