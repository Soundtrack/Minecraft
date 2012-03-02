package net.minecraft.src;

public class ModelBook extends ModelBase
{
    public ModelRenderer coverRight;
    public ModelRenderer coverLeft;
    public ModelRenderer pagesRight;
    public ModelRenderer pagesLeft;
    public ModelRenderer flippingPageRight;
    public ModelRenderer flippingPageLeft;
    public ModelRenderer bookSpine;

    public ModelBook()
    {
        coverRight = (new ModelRenderer(this)).setTextureOffset(0, 0).addBox(-6F, -5F, 0.0F, 6, 10, 0);
        coverLeft = (new ModelRenderer(this)).setTextureOffset(16, 0).addBox(0.0F, -5F, 0.0F, 6, 10, 0);
        bookSpine = (new ModelRenderer(this)).setTextureOffset(12, 0).addBox(-1F, -5F, 0.0F, 2, 10, 0);
        pagesRight = (new ModelRenderer(this)).setTextureOffset(0, 10).addBox(0.0F, -4F, -0.99F, 5, 8, 1);
        pagesLeft = (new ModelRenderer(this)).setTextureOffset(12, 10).addBox(0.0F, -4F, -0.01F, 5, 8, 1);
        flippingPageRight = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4F, 0.0F, 5, 8, 0);
        flippingPageLeft = (new ModelRenderer(this)).setTextureOffset(24, 10).addBox(0.0F, -4F, 0.0F, 5, 8, 0);
        coverRight.setRotationPoint(0.0F, 0.0F, -1F);
        coverLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
        bookSpine.rotateAngleY = 1.570796F;
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        coverRight.render(f5);
        coverLeft.render(f5);
        bookSpine.render(f5);
        pagesRight.render(f5);
        pagesLeft.render(f5);
        flippingPageRight.render(f5);
        flippingPageLeft.render(f5);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        float f6 = (MathHelper.sin(f * 0.02F) * 0.1F + 1.25F) * f3;
        coverRight.rotateAngleY = 3.141593F + f6;
        coverLeft.rotateAngleY = -f6;
        pagesRight.rotateAngleY = f6;
        pagesLeft.rotateAngleY = -f6;
        flippingPageRight.rotateAngleY = f6 - f6 * 2.0F * f1;
        flippingPageLeft.rotateAngleY = f6 - f6 * 2.0F * f2;
        pagesRight.rotationPointX = MathHelper.sin(f6);
        pagesLeft.rotationPointX = MathHelper.sin(f6);
        flippingPageRight.rotationPointX = MathHelper.sin(f6);
        flippingPageLeft.rotationPointX = MathHelper.sin(f6);
    }
}
