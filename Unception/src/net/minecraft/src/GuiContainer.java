package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainer extends GuiScreen
{
    protected static RenderItem itemRenderer = new RenderItem();
    protected int xSize;
    protected int ySize;
    public Container inventorySlots;
    protected int guiLeft;
    protected int guiTop;

    public GuiContainer(Container container)
    {
        xSize = 176;
        ySize = 166;
        inventorySlots = container;
    }

    public void initGui()
    {
        super.initGui();
        mc.thePlayer.craftingInventory = inventorySlots;
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        int k = guiLeft;
        int l = guiTop;
        drawGuiContainerBackgroundLayer(f, i, j);
        RenderHelper.func_41089_c();
        GL11.glPushMatrix();
        GL11.glTranslatef(k, l, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        Slot slot = null;
        int i1 = 240;
        int k1 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float)i1 / 1.0F, (float)k1 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        for (int j1 = 0; j1 < inventorySlots.inventorySlots.size(); j1++)
        {
            Slot slot1 = (Slot)inventorySlots.inventorySlots.get(j1);
            drawSlotInventory(slot1);
            if (getIsMouseOverSlot(slot1, i, j))
            {
                slot = slot1;
                GL11.glDisable(2896 /*GL_LIGHTING*/);
                GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
                int l1 = slot1.xDisplayPosition;
                int i2 = slot1.yDisplayPosition;
                drawGradientRect(l1, i2, l1 + 16, i2 + 16, 0x80ffffff, 0x80ffffff);
                GL11.glEnable(2896 /*GL_LIGHTING*/);
                GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
            }
        }

        InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
        if (inventoryplayer.getItemStack() != null)
        {
            GL11.glTranslatef(0.0F, 0.0F, 32F);
            zLevel = 200F;
            itemRenderer.zLevel = 200F;
            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), i - k - 8, j - l - 8);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), i - k - 8, j - l - 8);
            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
        }
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896 /*GL_LIGHTING*/);
        GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
        drawGuiContainerForegroundLayer();
        if (inventoryplayer.getItemStack() == null && slot != null && slot.getHasStack())
        {
            ItemStack itemstack = slot.getStack();
            List list = itemstack.getItemNameandInformation();
            if (list.size() > 0)
            {
                int j2 = 0;
                for (int k2 = 0; k2 < list.size(); k2++)
                {
                    int i3 = fontRenderer.getStringWidth((String)list.get(k2));
                    if (i3 > j2)
                    {
                        j2 = i3;
                    }
                }

                int l2 = (i - k) + 12;
                int j3 = j - l - 12;
                int k3 = j2;
                int l3 = 8;
                if (list.size() > 1)
                {
                    l3 += 2 + (list.size() - 1) * 10;
                }
                zLevel = 300F;
                itemRenderer.zLevel = 300F;
                int i4 = 0xf0100010;
                drawGradientRect(l2 - 3, j3 - 4, l2 + k3 + 3, j3 - 3, i4, i4);
                drawGradientRect(l2 - 3, j3 + l3 + 3, l2 + k3 + 3, j3 + l3 + 4, i4, i4);
                drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, j3 + l3 + 3, i4, i4);
                drawGradientRect(l2 - 4, j3 - 3, l2 - 3, j3 + l3 + 3, i4, i4);
                drawGradientRect(l2 + k3 + 3, j3 - 3, l2 + k3 + 4, j3 + l3 + 3, i4, i4);
                int j4 = 0x505000ff;
                int k4 = (j4 & 0xfefefe) >> 1 | j4 & 0xff000000;
                drawGradientRect(l2 - 3, (j3 - 3) + 1, (l2 - 3) + 1, (j3 + l3 + 3) - 1, j4, k4);
                drawGradientRect(l2 + k3 + 2, (j3 - 3) + 1, l2 + k3 + 3, (j3 + l3 + 3) - 1, j4, k4);
                drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, (j3 - 3) + 1, j4, j4);
                drawGradientRect(l2 - 3, j3 + l3 + 2, l2 + k3 + 3, j3 + l3 + 3, k4, k4);
                for (int l4 = 0; l4 < list.size(); l4++)
                {
                    String s = (String)list.get(l4);
                    if (l4 == 0)
                    {
                        s = (new StringBuilder()).append("\247").append(Integer.toHexString(itemstack.func_40707_s().field_40535_e)).append(s).toString();
                    }
                    else
                    {
                        s = (new StringBuilder()).append("\2477").append(s).toString();
                    }
                    fontRenderer.drawStringWithShadow(s, l2, j3, -1);
                    if (l4 == 0)
                    {
                        j3 += 2;
                    }
                    j3 += 10;
                }

                zLevel = 0.0F;
                itemRenderer.zLevel = 0.0F;
            }
        }
        GL11.glPopMatrix();
        super.drawScreen(i, j, f);
        GL11.glEnable(2896 /*GL_LIGHTING*/);
        GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
    }

    protected void drawGuiContainerForegroundLayer()
    {
    }

    protected abstract void drawGuiContainerBackgroundLayer(float f, int i, int j);

    private void drawSlotInventory(Slot slot)
    {
        int i = slot.xDisplayPosition;
        int j = slot.yDisplayPosition;
        ItemStack itemstack = slot.getStack();
        boolean flag = false;
        int k = i;
        int i1 = j;
        zLevel = 100F;
        itemRenderer.zLevel = 100F;
        if (itemstack == null)
        {
            int j1 = slot.getBackgroundIconIndex();
            if (j1 >= 0)
            {
                GL11.glDisable(2896 /*GL_LIGHTING*/);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
                drawTexturedModalRect(k, i1, (j1 % 16) * 16, (j1 / 16) * 16, 16, 16);
                GL11.glEnable(2896 /*GL_LIGHTING*/);
                flag = true;
            }
        }
        if (!flag)
        {
            itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, k, i1);
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, k, i1);
        }
        itemRenderer.zLevel = 0.0F;
        zLevel = 0.0F;
        if (this == null)
        {
            zLevel = 100F;
            itemRenderer.zLevel = 100F;
            if (itemstack == null)
            {
                int l = slot.getBackgroundIconIndex();
                if (l >= 0)
                {
                    GL11.glDisable(2896 /*GL_LIGHTING*/);
                    mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
                    drawTexturedModalRect(i, j, (l % 16) * 16, (l / 16) * 16, 16, 16);
                    GL11.glEnable(2896 /*GL_LIGHTING*/);
                    flag = true;
                }
            }
            if (!flag)
            {
                itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
                itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
            }
            itemRenderer.zLevel = 0.0F;
            zLevel = 0.0F;
        }
    }

    private Slot getSlotAtPosition(int i, int j)
    {
        for (int k = 0; k < inventorySlots.inventorySlots.size(); k++)
        {
            Slot slot = (Slot)inventorySlots.inventorySlots.get(k);
            if (getIsMouseOverSlot(slot, i, j))
            {
                return slot;
            }
        }

        return null;
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        if (k == 0 || k == 1)
        {
            Slot slot = getSlotAtPosition(i, j);
            int l = guiLeft;
            int i1 = guiTop;
            boolean flag = i < l || j < i1 || i >= l + xSize || j >= i1 + ySize;
            int j1 = -1;
            if (slot != null)
            {
                j1 = slot.slotNumber;
            }
            if (flag)
            {
                j1 = -999;
            }
            if (j1 != -1)
            {
                boolean flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                handleMouseClick(slot, j1, k, flag1);
            }
        }
    }

    private boolean getIsMouseOverSlot(Slot slot, int i, int j)
    {
        int k = guiLeft;
        int l = guiTop;
        i -= k;
        j -= l;
        return i >= slot.xDisplayPosition - 1 && i < slot.xDisplayPosition + 16 + 1 && j >= slot.yDisplayPosition - 1 && j < slot.yDisplayPosition + 16 + 1;
    }

    protected void handleMouseClick(Slot slot, int i, int j, boolean flag)
    {
        if (slot != null)
        {
            i = slot.slotNumber;
        }
        mc.playerController.windowClick(inventorySlots.windowId, i, j, flag, mc.thePlayer);
    }

    protected void keyTyped(char c, int i)
    {
        if (i == 1 || i == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.thePlayer.closeScreen();
        }
    }

    public void onGuiClosed()
    {
        if (mc.thePlayer == null)
        {
            return;
        }
        else
        {
            inventorySlots.onCraftGuiClosed(mc.thePlayer);
            mc.playerController.func_20086_a(inventorySlots.windowId, mc.thePlayer);
            return;
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void updateScreen()
    {
        super.updateScreen();
        if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
        {
            mc.thePlayer.closeScreen();
        }
    }
}
