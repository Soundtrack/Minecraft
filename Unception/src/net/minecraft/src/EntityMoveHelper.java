package net.minecraft.src;

public class EntityMoveHelper
{
    private EntityLiving entity;
    private double posX;
    private double posY;
    private double posZ;
    private float speed;
    private boolean field_46036_f;

    public EntityMoveHelper(EntityLiving entityliving, float f)
    {
        field_46036_f = false;
        entity = entityliving;
        posX = entityliving.posX;
        posY = entityliving.posY;
        posZ = entityliving.posZ;
        speed = f;
    }

    public void func_46035_a(double d, double d1, double d2)
    {
        posX = d;
        posY = d1;
        posZ = d2;
        field_46036_f = true;
    }

    public void setMoveSpeed(float f)
    {
        speed = f;
    }

    public void onUpdateMoveHelper()
    {
        entity.setMoveForward(0.0F);
        if (!field_46036_f)
        {
            return;
        }
        field_46036_f = false;
        int i = MathHelper.floor_double(entity.boundingBox.minY + 0.5D);
        double d = posX - entity.posX;
        double d1 = posZ - entity.posZ;
        double d2 = posY - (double)i;
        float f = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
        float f1;
        for (f1 = f - entity.rotationYaw; f1 < -180F; f1 += 360F) { }
        for (; f1 >= 180F; f1 -= 360F) { }
        if (f1 > 30F)
        {
            f1 = 30F;
        }
        if (f1 < -30F)
        {
            f1 = -30F;
        }
        entity.rotationYaw += f1;
        entity.setMoveForward(speed);
        entity.setIsJumping(d2 > 0.0D);
    }
}
