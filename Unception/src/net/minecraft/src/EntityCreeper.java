package net.minecraft.src;

import java.util.Random;

public class EntityCreeper extends EntityMob
{
    int timeSinceIgnited;
    int lastActiveTime;

    public EntityCreeper(World world)
    {
        super(world);
        texture = "/mob/creeper.png";
    }

    public int getMaxHealth()
    {
        return 20;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte) - 1));
        dataWatcher.addObject(17, Byte.valueOf((byte)0));
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        if (dataWatcher.getWatchableObjectByte(17) == 1)
        {
            nbttagcompound.setBoolean("powered", true);
        }
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        dataWatcher.updateObject(17, Byte.valueOf((byte)(nbttagcompound.getBoolean("powered") ? 1 : 0)));
    }

    protected void attackBlockedEntity(Entity entity, float f)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        if (timeSinceIgnited > 0)
        {
            setCreeperState(-1);
            timeSinceIgnited--;
            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }
        }
    }

    public void onUpdate()
    {
        lastActiveTime = timeSinceIgnited;
        if (worldObj.isRemote)
        {
            int i = getCreeperState();
            if (i > 0 && timeSinceIgnited == 0)
            {
                worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
            }
            timeSinceIgnited += i;
            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }
            if (timeSinceIgnited >= 30)
            {
                timeSinceIgnited = 30;
            }
        }
        super.onUpdate();
        if (entityToAttack == null && timeSinceIgnited > 0)
        {
            setCreeperState(-1);
            timeSinceIgnited--;
            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }
        }
    }

    protected String getHurtSound()
    {
        return "mob.creeper";
    }

    protected String getDeathSound()
    {
        return "mob.creeperdeath";
    }

    public void onDeath(DamageSource damagesource)
    {
        super.onDeath(damagesource);
        if (damagesource.getEntity() instanceof EntitySkeleton)
        {
            dropItem(Item.record13.shiftedIndex + rand.nextInt(10), 1);
        }
    }

    protected void attackEntity(Entity entity, float f)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        int i = getCreeperState();
        if (i <= 0 && f < 3F || i > 0 && f < 7F)
        {
            if (timeSinceIgnited == 0)
            {
                worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
            }
            setCreeperState(1);
            timeSinceIgnited++;
            if (timeSinceIgnited >= 30)
            {
                if (getPowered())
                {
                    worldObj.createExplosion(this, posX, posY, posZ, 6F);
                }
                else
                {
                    worldObj.createExplosion(this, posX, posY, posZ, 3F);
                }
                setEntityDead();
            }
            hasAttacked = true;
        }
        else
        {
            setCreeperState(-1);
            timeSinceIgnited--;
            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }
        }
    }

    public boolean getPowered()
    {
        return dataWatcher.getWatchableObjectByte(17) == 1;
    }

    public float setCreeperFlashTime(float f)
    {
        return ((float)lastActiveTime + (float)(timeSinceIgnited - lastActiveTime) * f) / 28F;
    }

    protected int getDropItemId()
    {
        return Item.gunpowder.shiftedIndex;
    }

    private int getCreeperState()
    {
        return dataWatcher.getWatchableObjectByte(16);
    }

    private void setCreeperState(int i)
    {
        dataWatcher.updateObject(16, Byte.valueOf((byte)i));
    }

    public void onStruckByLightning(EntityLightningBolt entitylightningbolt)
    {
        super.onStruckByLightning(entitylightningbolt);
        dataWatcher.updateObject(17, Byte.valueOf((byte)1));
    }
}
