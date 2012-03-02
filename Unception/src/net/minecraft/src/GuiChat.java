package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiChat extends GuiScreen
{
    protected String message;
    private int updateCounter;
    private static final String allowedCharacters;

    public GuiChat()
    {
        message = "";
        updateCounter = 0;
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen()
    {
        updateCounter++;
    }

    protected void keyTyped(char c, int i)
    {
        if (i == 1)
        {
            mc.displayGuiScreen(null);
            return;
        }
        if (i == 28)
        {
            String s = message.trim();
            if (s.length() > 0)
            {
                String s1 = message.trim();
                if (!mc.lineIsCommand(s1))
                {
                    mc.thePlayer.sendChatMessage(s1);
                }
            }
            mc.displayGuiScreen(null);
            return;
        }
        if (i == 14 && message.length() > 0)
        {
            message = message.substring(0, message.length() - 1);
        }
        if ((allowedCharacters.indexOf(c) >= 0 || c > ' ') && message.length() < 100)
        {
            message += c;
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawRect(2, height - 14, width - 2, height - 2, 0x80000000);
        drawString(fontRenderer, (new StringBuilder()).append("\2477Plus\2475+ ").append(message).append((updateCounter / 6) % 2 != 0 ? "" : "_").toString(), 4, height - 12, 0x000000);
        super.drawScreen(i, j, f);
    }
    
    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        if (k != 0)
        {
            return;
        }
        if (mc.ingameGUI.field_933_a == null)
        {
            return;
        }
        if (message.length() > 0 && !message.endsWith(" "))
        {
            message += " ";
        }
        message += mc.ingameGUI.field_933_a;
        byte byte0 = 100;
        if (message.length() > byte0)
        {
            message = message.substring(0, byte0);
        }
    }

    static
    {
        allowedCharacters = ChatAllowedCharacters.allowedCharacters;
    }
}
