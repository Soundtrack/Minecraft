package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CustomButton extends GuiButton { //Class name, change this to your class name

    public CustomButton(int i, int j, int k, String s)
    {  //Button with width of 120 and height of 20, change the name to your classname
  this(i, j, k, 120, 20, s);
    }

    public CustomButton(int i, int j, int k, int l, int i1, String s) {// Button with width and height you can choose yourself, change the name to your classname
        super(i, j, k, l, i1, s);
    }

    protected int getHoverState(boolean flag)
    {  //Hover
  byte byte0 = 1;
  if (!enabled)
  {
    byte0 = 0;
  }
  else if (flag)
  {
    byte0 = 2;
  }
  return byte0;
    }

    public void drawButton(Minecraft mc, int mx, int my)
    {  //Drawing the button
        FontRenderer fontrenderer = mc.fontRenderer; //Fontrenderer
        boolean flag = mx >= xPosition && my >= yPosition && mx < xPosition + width && my < yPosition + height;  //Flag, tells if your mouse is hovering the button
  if (flag)
  { //If your mouse is hovering the button, it makes darker bordered rect and white text
        drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + height, 1, 0x90FF, 0x800);
  drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xFF);
  }
  else { //If your mouse isn't hovering the button, bordered rect is lighter and text is a little bit darker than the hover text color
        drawBorderedRect(xPosition, yPosition, xPosition + width, yPosition + height, 1, 0x90FF, 0x600);
        drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xCC);
  }
    }

	private void drawBorderedRect(int xPosition, int yPosition, int i, int j,
			int k, int l, int m) {
		// TODO Auto-generated method stub
		
	}

}