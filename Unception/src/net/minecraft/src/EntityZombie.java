package net.minecraft.src;

import java.util.Random;

public class EntityZombie extends EntityMob
{
    public EntityZombie(World world)
    {
        super(world);
        texture = "/mob/zombie.png";
        moveSpeed = 0.5F;
        attackStrength = 4;
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackOnCollide(this, world, 16F));
        tasks.addTask(3, new EntityAIWander(this));
        tasks.addTask(4, new EntityAIWatchClosest(this, world, 8F));
        tasks.addTask(4, new EntityAILookIdle(this));
    }

    public int getMaxHealth()
    {
        return 20;
    }

    public int getTotalArmorValue()
    {
        return 2;
    }

    protected boolean isAIEnabled()
    {
        return false;
    }

    public void onLivingUpdate()
    {
        if (worldObj.isDaytime() && !worldObj.isRemote)
        {
            float f = getEntityBrightness(1.0F);
            if (f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F)
            {
                setFire(8);
            }
        }
        super.onLivingUpdate();
    }

    protected String getLivingSound()
    {
        return "mob.zombie";
    }

    protected String getHurtSound()
    {
        return "mob.zombiehurt";
    }

    protected String getDeathSound()
    {
        return "mob.zombiedeath";
    }

    protected int getDropItemId()
    {
        return Item.rottenFlesh.shiftedIndex;
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }
}
