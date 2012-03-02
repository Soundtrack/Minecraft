package net.minecraft.src;

public class EntityLookHelper
{
    private EntityLiving entity;
    private float field_46149_b;
    private float field_46150_c;
    private boolean field_46147_d;
    private double posX;
    private double posY;
    private double posZ;

    public EntityLookHelper(EntityLiving entityliving)
    {
        field_46147_d = false;
        entity = entityliving;
    }

    public void func_46141_a(Entity entity, float f, float f1)
    {
        posX = entity.posX;
        if (entity instanceof EntityLiving)
        {
            posY = entity.posY + (double)((EntityLiving)entity).getEyeHeight();
        }
        else
        {
            posY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2D;
        }
        posZ = entity.posZ;
        field_46149_b = f;
        field_46150_c = f1;
        field_46147_d = true;
    }

    public void func_46143_a(double d, double d1, double d2, float f,
            float f1)
    {
        posX = d;
        posY = d1;
        posZ = d2;
        field_46149_b = f;
        field_46150_c = f1;
        field_46147_d = true;
    }

    public void func_46142_a()
    {
        entity.rotationPitch = 0.0F;
        if (!field_46147_d)
        {
            return;
        }
        else
        {
            field_46147_d = false;
            double d = posX - entity.posX;
            double d1 = posY - (entity.posY + (double)entity.getEyeHeight());
            double d2 = posZ - entity.posZ;
            double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
            float f = (float)((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
            float f1 = (float)(-((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D));
            entity.rotationPitch = func_46144_a(entity.rotationPitch, f1, field_46150_c);
            entity.rotationYaw = func_46144_a(entity.rotationYaw, f, field_46149_b);
            return;
        }
    }

    private float func_46144_a(float f, float f1, float f2)
    {
        float f3;
        for (f3 = f1 - f; f3 < -180F; f3 += 360F) { }
        for (; f3 >= 180F; f3 -= 360F) { }
        if (f3 > f2)
        {
            f3 = f2;
        }
        if (f3 < -f2)
        {
            f3 = -f2;
        }
        return f + f3;
    }
}
