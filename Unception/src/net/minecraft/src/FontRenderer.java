package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.Random;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class FontRenderer
{
    private int charWidth[];
    public int fontTextureName;
    public int FONT_HEIGHT;
    public Random fontRandom;
    private byte glyphWidth[];
    private final int glyphTextureName[] = new int[256];
    private int colorCode[];
    private int boundTextureName;
    private final RenderEngine renderEngine;
    private float posX;
    private float posY;
    private boolean unicodeFlag;
    private boolean bidiFlag;

    public FontRenderer(GameSettings gamesettings, String s, RenderEngine renderengine, boolean flag)
    {
        charWidth = new int[256];
        fontTextureName = 0;
        FONT_HEIGHT = 8;
        fontRandom = new Random();
        glyphWidth = new byte[0x10000];
        colorCode = new int[32];
        renderEngine = renderengine;
        unicodeFlag = flag;
        BufferedImage bufferedimage;
        try
        {
            bufferedimage = ImageIO.read((net.minecraft.src.RenderEngine.class).getResourceAsStream(s));
            InputStream inputstream = (net.minecraft.src.RenderEngine.class).getResourceAsStream("/font/glyph_sizes.bin");
            inputstream.read(glyphWidth);
        }
        catch (IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int ai[] = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, ai, 0, i);
        for (int k = 0; k < 256; k++)
        {
            int i1 = k % 16;
            int k1 = k / 16;
            int i2 = 7;
            do
            {
                if (i2 < 0)
                {
                    break;
                }
                int k2 = i1 * 8 + i2;
                boolean flag1 = true;
                for (int j3 = 0; j3 < 8 && flag1; j3++)
                {
                    int l3 = (k1 * 8 + j3) * i;
                    int j4 = ai[k2 + l3] & 0xff;
                    if (j4 > 0)
                    {
                        flag1 = false;
                    }
                }

                if (!flag1)
                {
                    break;
                }
                i2--;
            }
            while (true);
            if (k == 32)
            {
                i2 = 2;
            }
            charWidth[k] = i2 + 2;
        }

        fontTextureName = renderengine.allocateAndSetupTexture(bufferedimage);
        for (int l = 0; l < 32; l++)
        {
            int j1 = (l >> 3 & 1) * 85;
            int l1 = (l >> 2 & 1) * 170 + j1;
            int j2 = (l >> 1 & 1) * 170 + j1;
            int l2 = (l >> 0 & 1) * 170 + j1;
            if (l == 6)
            {
                l1 += 85;
            }
            if (gamesettings.anaglyph)
            {
                int i3 = (l1 * 30 + j2 * 59 + l2 * 11) / 100;
                int k3 = (l1 * 30 + j2 * 70) / 100;
                int i4 = (l1 * 30 + l2 * 70) / 100;
                l1 = i3;
                j2 = k3;
                l2 = i4;
            }
            if (l >= 16)
            {
                l1 /= 4;
                j2 /= 4;
                l2 /= 4;
            }
            colorCode[l] = (l1 & 0xff) << 16 | (j2 & 0xff) << 8 | l2 & 0xff;
        }
    }

    private void renderDefaultChar(int i)
    {
        float f = (i % 16) * 8;
        float f1 = (i / 16) * 8;
        if (boundTextureName != fontTextureName)
        {
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, fontTextureName);
            boundTextureName = fontTextureName;
        }
        float f2 = (float)charWidth[i] - 0.01F;
        GL11.glBegin(5);
        GL11.glTexCoord2f(f / 128F, f1 / 128F);
        GL11.glVertex3f(posX, posY, 0.0F);
        GL11.glTexCoord2f(f / 128F, (f1 + 7.99F) / 128F);
        GL11.glVertex3f(posX, posY + 7.99F, 0.0F);
        GL11.glTexCoord2f((f + f2) / 128F, f1 / 128F);
        GL11.glVertex3f(posX + f2, posY, 0.0F);
        GL11.glTexCoord2f((f + f2) / 128F, (f1 + 7.99F) / 128F);
        GL11.glVertex3f(posX + f2, posY + 7.99F, 0.0F);
        GL11.glEnd();
        posX += charWidth[i];
    }

    private void loadGlyphTexture(int i)
    {
        String s = String.format("/font/glyph_%02X.png", new Object[]
                {
                    Integer.valueOf(i)
                });
        BufferedImage bufferedimage;
        try
        {
            bufferedimage = ImageIO.read((net.minecraft.src.RenderEngine.class).getResourceAsStream(s.toString()));
        }
        catch (IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }
        glyphTextureName[i] = renderEngine.allocateAndSetupTexture(bufferedimage);
        boundTextureName = glyphTextureName[i];
    }

    private void renderUnicodeChar(char c)
    {
        if (glyphWidth[c] == 0)
        {
            return;
        }
        int i = c / 256;
        if (glyphTextureName[i] == 0)
        {
            loadGlyphTexture(i);
        }
        if (boundTextureName != glyphTextureName[i])
        {
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, glyphTextureName[i]);
            boundTextureName = glyphTextureName[i];
        }
        int j = glyphWidth[c] >>> 4;
        int k = glyphWidth[c] & 0xf;
        float f = j;
        float f1 = k + 1;
        float f2 = (float)((c % 16) * 16) + f;
        float f3 = ((c & 0xff) / 16) * 16;
        float f4 = f1 - f - 0.02F;
        GL11.glBegin(5);
        GL11.glTexCoord2f(f2 / 256F, f3 / 256F);
        GL11.glVertex3f(posX, posY, 0.0F);
        GL11.glTexCoord2f(f2 / 256F, (f3 + 15.98F) / 256F);
        GL11.glVertex3f(posX, posY + 7.99F, 0.0F);
        GL11.glTexCoord2f((f2 + f4) / 256F, f3 / 256F);
        GL11.glVertex3f(posX + f4 / 2.0F, posY, 0.0F);
        GL11.glTexCoord2f((f2 + f4) / 256F, (f3 + 15.98F) / 256F);
        GL11.glVertex3f(posX + f4 / 2.0F, posY + 7.99F, 0.0F);
        GL11.glEnd();
        posX += (f1 - f) / 2.0F + 1.0F;
    }

    public void drawStringWithShadow(String s, int i, int j, int k)
    {
        if (bidiFlag)
        {
            s = bidiReorder(s);
        }
        renderString(s, i + 1, j + 1, k, true);
        renderString(s, i, j, k, false);
    }

    public void drawString(String s, int i, int j, int k)
    {
        if (bidiFlag)
        {
            s = bidiReorder(s);
        }
        renderString(s, i, j, k, false);
    }

    private String bidiReorder(String s)
    {
        if (s == null || !Bidi.requiresBidi(s.toCharArray(), 0, s.length()))
        {
            return s;
        }
        Bidi bidi = new Bidi(s, -2);
        byte abyte0[] = new byte[bidi.getRunCount()];
        String as[] = new String[abyte0.length];
        for (int i = 0; i < abyte0.length; i++)
        {
            int j = bidi.getRunStart(i);
            int k = bidi.getRunLimit(i);
            int i1 = bidi.getRunLevel(i);
            String s1 = s.substring(j, k);
            abyte0[i] = (byte)i1;
            as[i] = s1;
        }

        String as1[] = (String[])as.clone();
        Bidi.reorderVisually(abyte0, 0, as, 0, abyte0.length);
        StringBuilder stringbuilder = new StringBuilder();
        label0:
        for (int l = 0; l < as.length; l++)
        {
            byte byte0 = abyte0[l];
            int j1 = 0;
            do
            {
                if (j1 >= as1.length)
                {
                    break;
                }
                if (as1[j1].equals(as[l]))
                {
                    byte0 = abyte0[j1];
                    break;
                }
                j1++;
            }
            while (true);
            if ((byte0 & 1) == 0)
            {
                stringbuilder.append(as[l]);
                continue;
            }
            j1 = as[l].length() - 1;
            do
            {
                if (j1 < 0)
                {
                    continue label0;
                }
                char c = as[l].charAt(j1);
                if (c == '(')
                {
                    c = ')';
                }
                else if (c == ')')
                {
                    c = '(';
                }
                stringbuilder.append(c);
                j1--;
            }
            while (true);
        }

        return stringbuilder.toString();
    }

    private void renderStringAtPos(String s, boolean flag)
    {
        boolean flag1 = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (c == '\247' && i + 1 < s.length())
            {
                int j = "0123456789abcdefk".indexOf(s.toLowerCase().charAt(i + 1));
                if (j == 16)
                {
                    flag1 = true;
                }
                else
                {
                    flag1 = false;
                    if (j < 0 || j > 15)
                    {
                        j = 15;
                    }
                    if (flag)
                    {
                        j += 16;
                    }
                    int l = colorCode[j];
                    GL11.glColor3f((float)(l >> 16) / 255F, (float)(l >> 8 & 0xff) / 255F, (float)(l & 0xff) / 255F);
                }
                i++;
                continue;
            }
            int k = ChatAllowedCharacters.allowedCharacters.indexOf(c);
            if (flag1 && k > 0)
            {
                int i1;
                do
                {
                    i1 = fontRandom.nextInt(ChatAllowedCharacters.allowedCharacters.length());
                }
                while (charWidth[k + 32] != charWidth[i1 + 32]);
                k = i1;
            }
            if (c == ' ')
            {
                posX += 4F;
                continue;
            }
            if (k > 0 && !unicodeFlag)
            {
                renderDefaultChar(k + 32);
            }
            else
            {
                renderUnicodeChar(c);
            }
        }
    }

    private void renderString(String s, int i, int j, int k, boolean flag)
    {
        if (s != null)
        {
            boundTextureName = 0;
            if ((k & 0xfc000000) == 0)
            {
                k |= 0xff000000;
            }
            if (flag)
            {
                k = (k & 0xfcfcfc) >> 2 | k & 0xff000000;
            }
            GL11.glColor4f((float)(k >> 16 & 0xff) / 255F, (float)(k >> 8 & 0xff) / 255F, (float)(k & 0xff) / 255F, (float)(k >> 24 & 0xff) / 255F);
            posX = i;
            posY = j;
            renderStringAtPos(s, flag);
        }
    }

    public int getStringWidth(String s)
    {
        if (s == null)
        {
            return 0;
        }
        int i = 0;
        for (int j = 0; j < s.length(); j++)
        {
            char c = s.charAt(j);
            if (c == '\247')
            {
                j++;
                continue;
            }
            int k = ChatAllowedCharacters.allowedCharacters.indexOf(c);
            if (k >= 0 && !unicodeFlag)
            {
                i += charWidth[k + 32];
                continue;
            }
            if (glyphWidth[c] == 0)
            {
                continue;
            }
            int l = glyphWidth[c] >> 4;
            int i1 = glyphWidth[c] & 0xf;
            if (i1 > 7)
            {
                i1 = 15;
                l = 0;
            }
            i1++;
            i += (i1 - l) / 2 + 1;
        }

        return i;
    }

    public void drawSplitString(String s, int i, int j, int k, int l)
    {
        if (bidiFlag)
        {
            s = bidiReorder(s);
        }
        func_46124_b(s, i, j, k, l);
    }

    private void func_46124_b(String s, int i, int j, int k, int l)
    {
        renderSplitString(s, i, j, k, l, false);
    }

    public void drawSplitString(String s, int i, int j, int k, int l, boolean flag)
    {
        if (bidiFlag)
        {
            s = bidiReorder(s);
        }
        renderSplitString(s, i, j, k, l, flag);
    }

    private void renderSplitString(String s, int i, int j, int k, int l, boolean flag)
    {
        String as[] = s.split("\n");
        if (as.length > 1)
        {
            for (int i1 = 0; i1 < as.length; i1++)
            {
                func_46124_b(as[i1], i, j, k, l);
                j += splitStringWidth(as[i1], k);
            }

            return;
        }
        String as1[] = s.split(" ");
        int j1 = 0;
        String s1 = "";
        do
        {
            if (j1 >= as1.length)
            {
                break;
            }
            String s2;
            for (s2 = (new StringBuilder()).append(s1).append(as1[j1++]).append(" ").toString(); j1 < as1.length && getStringWidth((new StringBuilder()).append(s2).append(as1[j1]).toString()) < k; s2 = (new StringBuilder()).append(s2).append(as1[j1++]).append(" ").toString()) { }
            int k1;
            for (; getStringWidth(s2) > k; s2 = (new StringBuilder()).append(s1).append(s2.substring(k1)).toString())
            {
                for (k1 = 0; getStringWidth(s2.substring(0, k1 + 1)) <= k; k1++) { }
                if (s2.substring(0, k1).trim().length() <= 0)
                {
                    continue;
                }
                String s3 = s2.substring(0, k1);
                if (s3.lastIndexOf("\247") >= 0)
                {
                    s1 = (new StringBuilder()).append("\247").append(s3.charAt(s3.lastIndexOf("\247") + 1)).toString();
                }
                renderString(s3, i, j, l, flag);
                j += FONT_HEIGHT;
            }

            if (getStringWidth(s2.trim()) > 0)
            {
                if (s2.lastIndexOf("\247") >= 0)
                {
                    s1 = (new StringBuilder()).append("\247").append(s2.charAt(s2.lastIndexOf("\247") + 1)).toString();
                }
                renderString(s2, i, j, l, flag);
                j += FONT_HEIGHT;
            }
        }
        while (true);
    }

    public int splitStringWidth(String s, int i)
    {
        String as[] = s.split("\n");
        if (as.length > 1)
        {
            int j = 0;
            for (int k = 0; k < as.length; k++)
            {
                j += splitStringWidth(as[k], i);
            }

            return j;
        }
        String as1[] = s.split(" ");
        int l = 0;
        int i1 = 0;
        do
        {
            if (l >= as1.length)
            {
                break;
            }
            String s1;
            for (s1 = (new StringBuilder()).append(as1[l++]).append(" ").toString(); l < as1.length && getStringWidth((new StringBuilder()).append(s1).append(as1[l]).toString()) < i; s1 = (new StringBuilder()).append(s1).append(as1[l++]).append(" ").toString()) { }
            int j1;
            for (; getStringWidth(s1) > i; s1 = s1.substring(j1))
            {
                for (j1 = 0; getStringWidth(s1.substring(0, j1 + 1)) <= i; j1++) { }
                if (s1.substring(0, j1).trim().length() > 0)
                {
                    i1 += FONT_HEIGHT;
                }
            }

            if (s1.trim().length() > 0)
            {
                i1 += FONT_HEIGHT;
            }
        }
        while (true);
        if (i1 < FONT_HEIGHT)
        {
            i1 += FONT_HEIGHT;
        }
        return i1;
    }

    public void setUnicodeFlag(boolean flag)
    {
        unicodeFlag = flag;
    }

    public void setBidiFlag(boolean flag)
    {
        bidiFlag = flag;
    }
}
