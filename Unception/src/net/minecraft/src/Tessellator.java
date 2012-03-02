package net.minecraft.src;

import java.nio.*;
import org.lwjgl.opengl.*;

public class Tessellator
{
    private static boolean convertQuadsToTriangles = false;
    private static boolean tryVBO = false;
    private ByteBuffer byteBuffer;
    private IntBuffer intBuffer;
    private FloatBuffer floatBuffer;
    private ShortBuffer shortBuffer;
    private int rawBuffer[];
    private int vertexCount;
    private double textureU;
    private double textureV;
    private int brightness;
    private int color;
    private boolean hasColor;
    private boolean hasTexture;
    private boolean hasBrightness;
    private boolean hasNormals;
    private int rawBufferIndex;
    private int addedVertices;
    private boolean isColorDisabled;
    private int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private int normal;
    public static final Tessellator instance = new Tessellator(0x200000);
    private boolean isDrawing;
    private boolean useVBO;
    private IntBuffer vertexBuffers;
    private int vboIndex;
    private int vboCount;
    private int bufferSize;

    private Tessellator(int i)
    {
        vertexCount = 0;
        hasColor = false;
        hasTexture = false;
        hasBrightness = false;
        hasNormals = false;
        rawBufferIndex = 0;
        addedVertices = 0;
        isColorDisabled = false;
        isDrawing = false;
        useVBO = false;
        vboIndex = 0;
        vboCount = 10;
        bufferSize = i;
        byteBuffer = GLAllocation.createDirectByteBuffer(i * 4);
        intBuffer = byteBuffer.asIntBuffer();
        floatBuffer = byteBuffer.asFloatBuffer();
        shortBuffer = byteBuffer.asShortBuffer();
        rawBuffer = new int[i];
        useVBO = tryVBO && GLContext.getCapabilities().GL_ARB_vertex_buffer_object;
        if (useVBO)
        {
            vertexBuffers = GLAllocation.createDirectIntBuffer(vboCount);
            ARBVertexBufferObject.glGenBuffersARB(vertexBuffers);
        }
    }

    public int draw()
    {
        if (!isDrawing)
        {
            throw new IllegalStateException("Not tesselating!");
        }
        isDrawing = false;
        if (vertexCount > 0)
        {
            intBuffer.clear();
            intBuffer.put(rawBuffer, 0, rawBufferIndex);
            byteBuffer.position(0);
            byteBuffer.limit(rawBufferIndex * 4);
            if (useVBO)
            {
                vboIndex = (vboIndex + 1) % vboCount;
                ARBVertexBufferObject.glBindBufferARB(34962 /*GL_ARRAY_BUFFER_ARB*/, vertexBuffers.get(vboIndex));
                ARBVertexBufferObject.glBufferDataARB(34962 /*GL_ARRAY_BUFFER_ARB*/, byteBuffer, 35040 /*GL_STREAM_DRAW_ARB*/);
            }
            if (hasTexture)
            {
                if (useVBO)
                {
                    GL11.glTexCoordPointer(2, 5126 /*GL_FLOAT*/, 32, 12L);
                }
                else
                {
                    floatBuffer.position(3);
                    GL11.glTexCoordPointer(2, 32, floatBuffer);
                }
                GL11.glEnableClientState(32888 /*GL_TEXTURE_COORD_ARRAY_EXT*/);
            }
            if (hasBrightness)
            {
                OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapEnabled);
                if (useVBO)
                {
                    GL11.glTexCoordPointer(2, 5122 /*GL_SHORT*/, 32, 28L);
                }
                else
                {
                    shortBuffer.position(14);
                    GL11.glTexCoordPointer(2, 32, shortBuffer);
                }
                GL11.glEnableClientState(32888 /*GL_TEXTURE_COORD_ARRAY_EXT*/);
                OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapDisabled);
            }
            if (hasColor)
            {
                if (useVBO)
                {
                    GL11.glColorPointer(4, 5121 /*GL_UNSIGNED_BYTE*/, 32, 20L);
                }
                else
                {
                    byteBuffer.position(20);
                    GL11.glColorPointer(4, true, 32, byteBuffer);
                }
                GL11.glEnableClientState(32886 /*GL_COLOR_ARRAY_EXT*/);
            }
            if (hasNormals)
            {
                if (useVBO)
                {
                    GL11.glNormalPointer(5121 /*GL_UNSIGNED_BYTE*/, 32, 24L);
                }
                else
                {
                    byteBuffer.position(24);
                    GL11.glNormalPointer(32, byteBuffer);
                }
                GL11.glEnableClientState(32885 /*GL_NORMAL_ARRAY_EXT*/);
            }
            if (useVBO)
            {
                GL11.glVertexPointer(3, 5126 /*GL_FLOAT*/, 32, 0L);
            }
            else
            {
                floatBuffer.position(0);
                GL11.glVertexPointer(3, 32, floatBuffer);
            }
            GL11.glEnableClientState(32884 /*GL_VERTEX_ARRAY_EXT*/);
            if (drawMode == 7 && convertQuadsToTriangles)
            {
                GL11.glDrawArrays(4, 0, vertexCount);
            }
            else
            {
                GL11.glDrawArrays(drawMode, 0, vertexCount);
            }
            GL11.glDisableClientState(32884 /*GL_VERTEX_ARRAY_EXT*/);
            if (hasTexture)
            {
                GL11.glDisableClientState(32888 /*GL_TEXTURE_COORD_ARRAY_EXT*/);
            }
            if (hasBrightness)
            {
                OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapEnabled);
                GL11.glDisableClientState(32888 /*GL_TEXTURE_COORD_ARRAY_EXT*/);
                OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapDisabled);
            }
            if (hasColor)
            {
                GL11.glDisableClientState(32886 /*GL_COLOR_ARRAY_EXT*/);
            }
            if (hasNormals)
            {
                GL11.glDisableClientState(32885 /*GL_NORMAL_ARRAY_EXT*/);
            }
        }
        int i = rawBufferIndex * 4;
        reset();
        return i;
    }

    private void reset()
    {
        vertexCount = 0;
        byteBuffer.clear();
        rawBufferIndex = 0;
        addedVertices = 0;
    }

    public void startDrawingQuads()
    {
        startDrawing(7);
    }

    public void startDrawing(int i)
    {
        if (isDrawing)
        {
            throw new IllegalStateException("Already tesselating!");
        }
        else
        {
            isDrawing = true;
            reset();
            drawMode = i;
            hasNormals = false;
            hasColor = false;
            hasTexture = false;
            hasBrightness = false;
            isColorDisabled = false;
            return;
        }
    }

    public void setTextureUV(double d, double d1)
    {
        hasTexture = true;
        textureU = d;
        textureV = d1;
    }

    public void setBrightness(int i)
    {
        hasBrightness = true;
        brightness = i;
    }

    public void setColorOpaque_F(float f, float f1, float f2)
    {
        setColorOpaque((int)(f * 255F), (int)(f1 * 255F), (int)(f2 * 255F));
    }

    public void setColorRGBA_F(float f, float f1, float f2, float f3)
    {
        setColorRGBA((int)(f * 255F), (int)(f1 * 255F), (int)(f2 * 255F), (int)(f3 * 255F));
    }

    public void setColorOpaque(int i, int j, int k)
    {
        setColorRGBA(i, j, k, 255);
    }

    public void setColorRGBA(int i, int j, int k, int l)
    {
        if (isColorDisabled)
        {
            return;
        }
        if (i > 255)
        {
            i = 255;
        }
        if (j > 255)
        {
            j = 255;
        }
        if (k > 255)
        {
            k = 255;
        }
        if (l > 255)
        {
            l = 255;
        }
        if (i < 0)
        {
            i = 0;
        }
        if (j < 0)
        {
            j = 0;
        }
        if (k < 0)
        {
            k = 0;
        }
        if (l < 0)
        {
            l = 0;
        }
        hasColor = true;
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            color = l << 24 | k << 16 | j << 8 | i;
        }
        else
        {
            color = i << 24 | j << 16 | k << 8 | l;
        }
    }

    public void addVertexWithUV(double d, double d1, double d2, double d3, double d4)
    {
        setTextureUV(d3, d4);
        addVertex(d, d1, d2);
    }

    public void addVertex(double d, double d1, double d2)
    {
        addedVertices++;
        if (drawMode == 7 && convertQuadsToTriangles && addedVertices % 4 == 0)
        {
            for (int i = 0; i < 2; i++)
            {
                int j = 8 * (3 - i);
                if (hasTexture)
                {
                    rawBuffer[rawBufferIndex + 3] = rawBuffer[(rawBufferIndex - j) + 3];
                    rawBuffer[rawBufferIndex + 4] = rawBuffer[(rawBufferIndex - j) + 4];
                }
                if (hasBrightness)
                {
                    rawBuffer[rawBufferIndex + 7] = rawBuffer[(rawBufferIndex - j) + 7];
                }
                if (hasColor)
                {
                    rawBuffer[rawBufferIndex + 5] = rawBuffer[(rawBufferIndex - j) + 5];
                }
                rawBuffer[rawBufferIndex + 0] = rawBuffer[(rawBufferIndex - j) + 0];
                rawBuffer[rawBufferIndex + 1] = rawBuffer[(rawBufferIndex - j) + 1];
                rawBuffer[rawBufferIndex + 2] = rawBuffer[(rawBufferIndex - j) + 2];
                vertexCount++;
                rawBufferIndex += 8;
            }
        }
        if (hasTexture)
        {
            rawBuffer[rawBufferIndex + 3] = Float.floatToRawIntBits((float)textureU);
            rawBuffer[rawBufferIndex + 4] = Float.floatToRawIntBits((float)textureV);
        }
        if (hasBrightness)
        {
            rawBuffer[rawBufferIndex + 7] = brightness;
        }
        if (hasColor)
        {
            rawBuffer[rawBufferIndex + 5] = color;
        }
        if (hasNormals)
        {
            rawBuffer[rawBufferIndex + 6] = normal;
        }
        rawBuffer[rawBufferIndex + 0] = Float.floatToRawIntBits((float)(d + xOffset));
        rawBuffer[rawBufferIndex + 1] = Float.floatToRawIntBits((float)(d1 + yOffset));
        rawBuffer[rawBufferIndex + 2] = Float.floatToRawIntBits((float)(d2 + zOffset));
        rawBufferIndex += 8;
        vertexCount++;
        if (vertexCount % 4 == 0 && rawBufferIndex >= bufferSize - 32)
        {
            draw();
            isDrawing = true;
        }
    }

    public void setColorOpaque_I(int i)
    {
        int j = i >> 16 & 0xff;
        int k = i >> 8 & 0xff;
        int l = i & 0xff;
        setColorOpaque(j, k, l);
    }

    public void setColorRGBA_I(int i, int j)
    {
        int k = i >> 16 & 0xff;
        int l = i >> 8 & 0xff;
        int i1 = i & 0xff;
        setColorRGBA(k, l, i1, j);
    }

    public void disableColor()
    {
        isColorDisabled = true;
    }

    public void setNormal(float f, float f1, float f2)
    {
        hasNormals = true;
        byte byte0 = (byte)(int)(f * 127F);
        byte byte1 = (byte)(int)(f1 * 127F);
        byte byte2 = (byte)(int)(f2 * 127F);
        normal = byte0 & 0xff | (byte1 & 0xff) << 8 | (byte2 & 0xff) << 16;
    }

    public void setTranslationD(double d, double d1, double d2)
    {
        xOffset = d;
        yOffset = d1;
        zOffset = d2;
    }

    public void setTranslationF(float f, float f1, float f2)
    {
        xOffset += f;
        yOffset += f1;
        zOffset += f2;
    }
}
