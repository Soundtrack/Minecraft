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
    	drawRect(2, height - 16, 12, height - 26, 0xFF000000);
        drawRect(14, height - 16, 24, height - 26, 0xFF0000AA);
        drawRect(26, height - 16, 36, height - 26, 0xFF00AA00);
        drawRect(38, height - 16, 48, height - 26, 0xFF00AAAA);
        drawRect(50, height - 16, 60, height - 26, 0xFFAA0000);
        drawRect(62, height - 16, 72, height - 26, 0xFFAA00AA);
        drawRect(74, height - 16, 84, height - 26, 0xFFFFAA00);
        drawRect(86, height - 16, 96, height - 26, 0xFFAAAAAA);
        drawRect(98, height - 16, 108, height - 26, 0xFF555555);
        drawRect(110, height - 16, 120, height - 26, 0xFF5555FF);
        drawRect(122, height - 16, 132, height - 26, 0xFF55FF55);
        drawRect(134, height - 16, 144, height - 26, 0xFF55FFFF);
        drawRect(146, height - 16, 156, height - 26, 0xFFFF5555);
        drawRect(158, height - 16, 168, height - 26, 0xFFFF55FF);
        drawRect(170, height - 16, 180, height - 26, 0xFFFFFF55);
        drawRect(182, height - 16, 192, height - 26, 0xFFFFFFFF);
        drawRect(194, height - 16, 233, height - 26, 0xFF000000);
        drawString(fontRenderer, "Random", 196, height - 24, 0xFFFFFF);
        drawRect(2, height - 14, width - 2, height - 2, 0x80000000);
        drawRect(2, height - 14, width - 2, height - 2, 0x80000000);
        drawString(fontRenderer, (new StringBuilder()).append("> ").append(message).append((updateCounter / 6) % 2 != 0 ? "" : "_").toString(), 4, height - 12, 0xe0e0e0);
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
        if(i > 2 && i < 12 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2470").toString(); 
        }
        if(i > 14 && i < 24 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2471").toString(); 
        }
        if(i > 26 && i < 36 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2472").toString(); 
        }
        if(i > 38 && i < 48 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2473").toString(); 
        }
        if(i > 50 && i < 60 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2474").toString(); 
        }
        if(i > 62 && i < 72 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2475").toString(); 
        }
        if(i > 74 && i < 84 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2476").toString(); 
        }
        if(i > 86 && i < 96 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2477").toString(); 
        }
        if(i > 98 && i < 108 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2478").toString(); 
        }
        if(i > 110 && i < 120 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\2479").toString(); 
        }
        if(i > 122 && i < 132 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\247a").toString(); 
        }
        if(i > 134 && i < 144 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\247b").toString(); 
        }
        if(i > 146 && i < 156 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\247c").toString(); 
        }
        if(i > 158 && i < 168 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\247d").toString(); 
        }
        if(i > 170 && i < 180 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\247e").toString(); 
        }
        if(i > 182 && i < 192 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\247f").toString(); 
        }
        if(i > 194 && i < 233 && j > height - 26 && j < height - 16){
            message = (new StringBuilder()).append(message).append("\247k").toString(); 
        }
    }

    static
    {
        allowedCharacters = ChatAllowedCharacters.allowedCharacters;
    }
}
