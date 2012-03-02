package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiConsole extends GuiScreen
{

    public GuiConsole()
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
  if(c == '\026') {
    String cb = GuiScreen.getClipboardString();
    if(cb != null) {
    message += cb;
    }
  }    
  if(i == 1)
  {
    mc.displayGuiScreen(null);
    return;
  }
  if(i == 28)
  {
    String s = message.trim();
    //commands
    //-------------------------------
    if(s.startsWith("help")){ 
        mc.thePlayer.addChatMessage("\2477[\2475Console\2477]\2475Welcome To Fatalily Greifing Client Help Menu.");
    mc.thePlayer.addChatMessage("\2477[\2475Console\2477]\2475(tracer)\2477aVo Tracer's.");
    mc.thePlayer.addChatMessage("\2477[\2475Console\2477]\2475(chest)\2477Outlines A Chest Or Chests.");
    mc.thePlayer.addChatMessage("\2477[\2475Console\2477]\2475(noweather)\2477You Know What It Does -.-.");
    mc.thePlayer.addChatMessage("\2477[\2475Console\2477]\2475(compass)\2477It's A Compass.");
    mc.thePlayer.addChatMessage("\2477[\2475Console\2477]\2475(help 2)\2477For Page 2 Of The Command List.");
    }
    if(s.startsWith("noswing")){
    	GuiIngame.NoSwing = !GuiIngame.NoSwing;
    }
    if(s.startsWith("weather")){
    	GuiIngame.Weather = !GuiIngame.Weather;
    }
    if(s.startsWith("chest")){
    	GuiIngame.chestfinder = !GuiIngame.chestfinder;
    }
    if(s.startsWith("compass")){
  	GuiIngame.compass = !GuiIngame.compass;
    }
  	if(s.startsWith("tracer")){
	GuiIngame.Tracer = !GuiIngame.Tracer;
  	}
    //---------------------------------------
    //commands
    mc.displayGuiScreen(null);
    return;
  }
  if(i == 14 && message.length() > 0)
  {
    message = message.substring(0, message.length() - 1);
  }
  if(allowedCharacters.indexOf(c) >= 0 && this.message.length() < 100) {
    this.message = this.message + c;
  }
    }

    public void drawScreen(int i, int j, float f)
    {
    	drawRect(2, 2, width - 2, 14, 0x80000000);
    	drawString(fontRenderer, (new StringBuilder()).append("\2477[\2475Console\2477]\2475 ").append(message).append((updateCounter / 6) % 2 != 0 ? "" : "_").toString(), 4, 4, 0x00E9F5);
  super.drawScreen(i, j, f);
    }

    protected void mouseClicked(int var1, int var2, int var3) {
  if(var3 == 0) {
  if(this.mc.ingameGUI.field_933_a != null) {
    if(this.message.length() > 0 && !this.message.endsWith(" ")) {
  this.message = this.message + " ";
    }

    this.message = this.message + this.mc.ingameGUI.field_933_a;
    byte var4 = 100;
    if(this.message.length() > var4) {
  this.message = this.message.substring(0, var4);
    }
  } else {
    super.mouseClicked(var1, var2, var3);
  }
  }

  }

    protected String message;
    private int updateCounter;
    private static final String allowedCharacters;

    static 
    {
  allowedCharacters = ChatAllowedCharacters.allowedCharacters;
    }
}