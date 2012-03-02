package net.minecraft.src;

public class ModelBox
{
    private PositionTextureVertex vertexPositions[];
    private TexturedQuad quadList[];
    public final float posX1;
    public final float posY1;
    public final float posZ1;
    public final float posX2;
    public final float posY2;
    public final float posZ2;
    public String field_40673_g;

    public ModelBox(ModelRenderer modelrenderer, int i, int j, float f, float f1, float f2, int k,
            int l, int i1, float f3)
    {
        posX1 = f;
        posY1 = f1;
        posZ1 = f2;
        posX2 = f + (float)k;
        posY2 = f1 + (float)l;
        posZ2 = f2 + (float)i1;
        vertexPositions = new PositionTextureVertex[8];
        quadList = new TexturedQuad[6];
        float f4 = f + (float)k;
        float f5 = f1 + (float)l;
        float f6 = f2 + (float)i1;
        f -= f3;
        f1 -= f3;
        f2 -= f3;
        f4 += f3;
        f5 += f3;
        f6 += f3;
        if (modelrenderer.mirror)
        {
            float f7 = f4;
            f4 = f;
            f = f7;
        }
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, f1, f2, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f4, f1, f2, 0.0F, 8F);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(f4, f5, f2, 8F, 8F);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(f, f5, f2, 8F, 0.0F);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, f1, f6, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f4, f1, f6, 0.0F, 8F);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(f4, f5, f6, 8F, 8F);
        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(f, f5, f6, 8F, 0.0F);
        vertexPositions[0] = positiontexturevertex;
        vertexPositions[1] = positiontexturevertex1;
        vertexPositions[2] = positiontexturevertex2;
        vertexPositions[3] = positiontexturevertex3;
        vertexPositions[4] = positiontexturevertex4;
        vertexPositions[5] = positiontexturevertex5;
        vertexPositions[6] = positiontexturevertex6;
        vertexPositions[7] = positiontexturevertex7;
        quadList[0] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex5, positiontexturevertex1, positiontexturevertex2, positiontexturevertex6
                }, i + i1 + k, j + i1, i + i1 + k + i1, j + i1 + l, modelrenderer.textureWidth, modelrenderer.textureHeight);
        quadList[1] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex, positiontexturevertex4, positiontexturevertex7, positiontexturevertex3
                }, i + 0, j + i1, i + i1, j + i1 + l, modelrenderer.textureWidth, modelrenderer.textureHeight);
        quadList[2] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex5, positiontexturevertex4, positiontexturevertex, positiontexturevertex1
                }, i + i1, j + 0, i + i1 + k, j + i1, modelrenderer.textureWidth, modelrenderer.textureHeight);
        quadList[3] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex2, positiontexturevertex3, positiontexturevertex7, positiontexturevertex6
                }, i + i1 + k, j + i1, i + i1 + k + k, j + 0, modelrenderer.textureWidth, modelrenderer.textureHeight);
        quadList[4] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex1, positiontexturevertex, positiontexturevertex3, positiontexturevertex2
                }, i + i1, j + i1, i + i1 + k, j + i1 + l, modelrenderer.textureWidth, modelrenderer.textureHeight);
        quadList[5] = new TexturedQuad(new PositionTextureVertex[]
                {
                    positiontexturevertex4, positiontexturevertex5, positiontexturevertex6, positiontexturevertex7
                }, i + i1 + k + i1, j + i1, i + i1 + k + i1 + k, j + i1 + l, modelrenderer.textureWidth, modelrenderer.textureHeight);
        if (modelrenderer.mirror)
        {
            for (int j1 = 0; j1 < quadList.length; j1++)
            {
                quadList[j1].flipFace();
            }
        }
    }

    public void render(Tessellator tessellator, float f)
    {
        for (int i = 0; i < quadList.length; i++)
        {
            quadList[i].draw(tessellator, f);
        }
    }

    public ModelBox func_40671_a(String s)
    {
        field_40673_g = s;
        return this;
    }
}
