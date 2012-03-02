package net.minecraft.src;

import java.util.Random;

public class EntitySpider extends EntityMob
{
    public EntitySpider(World world)
    {
        super(world);
        texture = "/mob/spider.png";
        setSize(1.4F, 0.9F);
        moveSpeed = 0.8F;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)0));
    }

    public void onLivingUpdate()
    {
        super.onLivingUpdate();
    }

    public void onUpdate()
    {
        super.onUpdate();
        if (!worldObj.multiplayerWorld)
        {
            func_40148_a(isCollidedHorizontally);
        }
    }

    public int getMaxHealth()
    {
        return 16;
    }

    public double getMountedYOffset()
    {
        return (double)height * 0.75D - 0.5D;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected Entity findPlayerToAttack()
    {
        float f = getEntityBrightness(1.0F);
        if (f < 0.5F)
        {
            double d = 16D;
            return worldObj.getClosestVulnerablePlayerToEntity(this, d);
        }
        else
        {
            return null;
        }
    }

    protected String getLivingSound()
    {
        return "mob.spider";
    }

    protected String getHurtSound()
    {
        return "mob.spider";
    }

    protected String getDeathSound()
    {
        return "mob.spiderdeath";
    }

    protected void attackEntity(Entity entity, float f)
    {
        float f1 = getEntityBrightness(1.0F);
        if (f1 > 0.5F && rand.nextInt(100) == 0)
        {
            entityToAttack = null;
            return;
        }
        if (f > 2.0F && f < 6F && rand.nextInt(10) == 0)
        {
            if (onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f2 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = (d / (double)f2) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                motionZ = (d1 / (double)f2) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                motionY = 0.40000000596046448D;
            }
        }
        else
        {
            super.attackEntity(entity, f);
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
    }

    protected int getDropItemId()
    {
        return Item.silk.shiftedIndex;
    }

    protected void dropFewItems(boolean flag, int i)
    {
        super.dropFewItems(flag, i);
        if (flag && (rand.nextInt(3) == 0 || rand.nextInt(1 + i) > 0))
        {
            dropItem(Item.spiderEye.shiftedIndex, 1);
        }
    }

    public boolean isOnLadder()
    {
        return func_40149_l_();
    }

    public void setInWeb()
    {
    }

    public float spiderScaleAmount()
    {
        return 1.0F;
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public boolean func_40126_a(PotionEffect potioneffect)
    {
        if (potioneffect.getPotionID() == Potion.poison.id)
        {
            return false;
        }
        else
        {
            return super.func_40126_a(potioneffect);
        }
    }

    public boolean func_40149_l_()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void func_40148_a(boolean flag)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);
        if (flag)
        {
            byte0 |= 1;
        }
        else
        {
            byte0 &= 0xfe;
        }
        dataWatcher.updateObject(16, Byte.valueOf(byte0));
    }
}
