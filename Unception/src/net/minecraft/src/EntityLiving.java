package net.minecraft.src;

import java.util.*;

public abstract class EntityLiving extends Entity
{
    public int heartsHalvesLife;
    public float field_9365_p;
    public float field_9363_r;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    public float field_46015_bf;
    public float field_46016_bg;
    protected float field_9362_u;
    protected float field_9361_v;
    protected float field_9360_w;
    protected float field_9359_x;
    protected boolean field_9358_y;
    protected String texture;
    protected boolean field_9355_A;
    protected float field_9353_B;
    protected String entityType;
    protected float field_9349_D;
    protected int scoreValue;
    protected float field_9345_F;
    public float landMovementFactor;
    public float jumpMovementFactor;
    public float prevSwingProgress;
    public float swingProgress;
    protected int health;
    public int prevHealth;
    protected int carryoverDamage;
    private int livingSoundTime;
    public int hurtTime;
    public int maxHurtTime;
    public float attackedAtYaw;
    public int deathTime;
    public int attackTime;
    public float prevCameraPitch;
    public float cameraPitch;
    protected boolean unused_flag;
    protected int experienceValue;
    public int field_9326_T;
    public float field_9325_U;
    public float field_705_Q;
    public float field_704_R;
    public float field_703_S;
    protected EntityPlayer attackingPlayer;
    protected int recentlyHit;
    protected EntityLiving entityLivingToAttack;
    public int arrowHitTempCounter;
    public int arrowHitTimer;
    protected HashMap activePotionsMap;
    private boolean field_39001_b;
    private int field_39002_c;
    private EntityLookHelper lookHelper;
    private EntityMoveHelper moveHelper;
    private EntityJumpHelper jumpHelper;
    private INavigate navigation;
    protected EntityAITasks tasks;
    protected int newPosRotationIncrements;
    protected double newPosX;
    protected double newPosY;
    protected double newPosZ;
    protected double newRotationYaw;
    protected double newRotationPitch;
    float field_9348_ae;
    protected int naturalArmorRating;
    protected int entityAge;
    protected float moveStrafing;
    protected float moveForward;
    protected float randomYawVelocity;
    protected boolean isJumping;
    protected float defaultPitch;
    protected float moveSpeed;
    private int field_39003_d;
    private Entity currentTarget;
    protected int numTicksToChaseTarget;

    public EntityLiving(World world)
    {
        super(world);
        heartsHalvesLife = 20;
        renderYawOffset = 0.0F;
        prevRenderYawOffset = 0.0F;
        field_46015_bf = 0.0F;
        field_46016_bg = 0.0F;
        field_9358_y = true;
        texture = "/mob/char.png";
        field_9355_A = true;
        field_9353_B = 0.0F;
        entityType = null;
        field_9349_D = 1.0F;
        scoreValue = 0;
        field_9345_F = 0.0F;
        landMovementFactor = 0.1F;
        jumpMovementFactor = 0.02F;
        attackedAtYaw = 0.0F;
        deathTime = 0;
        attackTime = 0;
        unused_flag = false;
        field_9326_T = -1;
        field_9325_U = (float)(Math.random() * 0.89999997615814209D + 0.10000000149011612D);
        attackingPlayer = null;
        recentlyHit = 0;
        entityLivingToAttack = null;
        arrowHitTempCounter = 0;
        arrowHitTimer = 0;
        activePotionsMap = new HashMap();
        field_39001_b = true;
        tasks = new EntityAITasks();
        field_9348_ae = 0.0F;
        naturalArmorRating = 0;
        entityAge = 0;
        isJumping = false;
        defaultPitch = 0.0F;
        moveSpeed = 0.7F;
        field_39003_d = 0;
        numTicksToChaseTarget = 0;
        health = getMaxHealth();
        preventEntitySpawning = true;
        lookHelper = new EntityLookHelper(this);
        moveHelper = new EntityMoveHelper(this, moveSpeed);
        jumpHelper = new EntityJumpHelper(this);
        navigation = new PathNavigate(this, world);
        field_9363_r = (float)(Math.random() + 1.0D) * 0.01F;
        setPosition(posX, posY, posZ);
        field_9365_p = (float)Math.random() * 12398F;
        rotationYaw = (float)(Math.random() * 3.1415927410125732D * 2D);
        field_46015_bf = rotationYaw;
        stepHeight = 0.5F;
    }

    public EntityLookHelper getLookHelper()
    {
        return lookHelper;
    }

    public EntityMoveHelper getMoveHelper()
    {
        return moveHelper;
    }

    public EntityJumpHelper getJumpHelper()
    {
        return jumpHelper;
    }

    public INavigate func_46012_aJ()
    {
        return navigation;
    }

    public Random getRNG()
    {
        return rand;
    }

    public EntityLiving func_46007_aL()
    {
        return entityLivingToAttack;
    }

    public int getAge()
    {
        return entityAge;
    }

    protected void entityInit()
    {
        dataWatcher.addObject(8, Integer.valueOf(field_39002_c));
    }

    public boolean canEntityBeSeen(Entity entity)
    {
        return worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ)) == null;
    }

    public String getEntityTexture()
    {
        return texture;
    }

    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    public boolean canBePushed()
    {
        return !isDead;
    }

    public float getEyeHeight()
    {
        return height * 0.85F;
    }

    public int getTalkInterval()
    {
        return 80;
    }

    public void playLivingSound()
    {
        String s = getLivingSound();
        if (s != null)
        {
            worldObj.playSoundAtEntity(this, s, getSoundVolume(), getSoundPitch());
        }
    }

    public void onEntityUpdate()
    {
        prevSwingProgress = swingProgress;
        super.onEntityUpdate();
        Profiler.startSection("mobBaseTick");
        if (rand.nextInt(1000) < livingSoundTime++)
        {
            livingSoundTime = -getTalkInterval();
            playLivingSound();
        }
        if (isEntityAlive() && isEntityInsideOpaqueBlock())
        {
            if (!attackEntityFrom(DamageSource.inWall, 1));
        }
        if (isImmuneToFire() || worldObj.isRemote)
        {
            extinguish();
        }
        if (isEntityAlive() && isInsideOfMaterial(Material.water) && !canBreatheUnderwater() && !activePotionsMap.containsKey(Integer.valueOf(Potion.waterBreathing.id)))
        {
            setAir(decreaseAirSupply(getAir()));
            if (getAir() == -20)
            {
                setAir(0);
                for (int i = 0; i < 8; i++)
                {
                    float f = rand.nextFloat() - rand.nextFloat();
                    float f1 = rand.nextFloat() - rand.nextFloat();
                    float f2 = rand.nextFloat() - rand.nextFloat();
                    worldObj.spawnParticle("bubble", posX + (double)f, posY + (double)f1, posZ + (double)f2, motionX, motionY, motionZ);
                }

                attackEntityFrom(DamageSource.drown, 2);
            }
            extinguish();
        }
        else
        {
            setAir(300);
        }
        prevCameraPitch = cameraPitch;
        if (attackTime > 0)
        {
            attackTime--;
        }
        if (hurtTime > 0)
        {
            hurtTime--;
        }
        if (heartsLife > 0)
        {
            heartsLife--;
        }
        if (health <= 0)
        {
            onDeathUpdate();
        }
        if (recentlyHit > 0)
        {
            recentlyHit--;
        }
        else
        {
            attackingPlayer = null;
        }
        updatePotionEffects();
        field_9359_x = field_9360_w;
        prevRenderYawOffset = renderYawOffset;
        field_46016_bg = field_46015_bf;
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        Profiler.endSection();
    }

    protected void onDeathUpdate()
    {
        deathTime++;
        if (deathTime == 20)
        {
            if (!worldObj.isRemote && (recentlyHit > 0 || isPlayer()) && !isChild())
            {
                for (int i = getExperiencePoints(attackingPlayer); i > 0;)
                {
                    int k = EntityXPOrb.getXPSplit(i);
                    i -= k;
                    worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, k));
                }
            }
            onEntityDeath();
            setEntityDead();
            for (int j = 0; j < 20; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("explode", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
        }
    }

    protected int decreaseAirSupply(int i)
    {
        return i - 1;
    }

    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return experienceValue;
    }

    protected boolean isPlayer()
    {
        return false;
    }

    public void spawnExplosionParticle()
    {
        for (int i = 0; i < 20; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            double d3 = 10D;
            worldObj.spawnParticle("explode", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - d * d3, (posY + (double)(rand.nextFloat() * height)) - d1 * d3, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - d2 * d3, d, d1, d2);
        }
    }

    public void updateRidden()
    {
        super.updateRidden();
        field_9362_u = field_9361_v;
        field_9361_v = 0.0F;
        fallDistance = 0.0F;
    }

    public void setPositionAndRotation2(double d, double d1, double d2, float f,
            float f1, int i)
    {
        yOffset = 0.0F;
        newPosX = d;
        newPosY = d1;
        newPosZ = d2;
        newRotationYaw = f;
        newRotationPitch = f1;
        newPosRotationIncrements = i;
    }

    public void onUpdate()
    {
        super.onUpdate();
        if (arrowHitTempCounter > 0)
        {
            if (arrowHitTimer <= 0)
            {
                arrowHitTimer = 60;
            }
            arrowHitTimer--;
            if (arrowHitTimer <= 0)
            {
                arrowHitTempCounter--;
            }
        }
        onLivingUpdate();
        double d = posX - prevPosX;
        double d1 = posZ - prevPosZ;
        float f = MathHelper.sqrt_double(d * d + d1 * d1);
        float f1 = renderYawOffset;
        float f2 = 0.0F;
        field_9362_u = field_9361_v;
        float f3 = 0.0F;
        if (f > 0.05F)
        {
            f3 = 1.0F;
            f2 = f * 3F;
            f1 = ((float)Math.atan2(d1, d) * 180F) / 3.141593F - 90F;
        }
        if (swingProgress > 0.0F)
        {
            f1 = rotationYaw;
        }
        if (!onGround)
        {
            f3 = 0.0F;
        }
        field_9361_v = field_9361_v + (f3 - field_9361_v) * 0.3F;
        float f4;
        for (f4 = f1 - renderYawOffset; f4 < -180F; f4 += 360F) { }
        for (; f4 >= 180F; f4 -= 360F) { }
        renderYawOffset += f4 * 0.3F;
        float f5;
        for (f5 = rotationYaw - renderYawOffset; f5 < -180F; f5 += 360F) { }
        for (; f5 >= 180F; f5 -= 360F) { }
        boolean flag = f5 < -90F || f5 >= 90F;
        if (f5 < -75F)
        {
            f5 = -75F;
        }
        if (f5 >= 75F)
        {
            f5 = 75F;
        }
        renderYawOffset = rotationYaw - f5;
        if (f5 * f5 > 2500F)
        {
            renderYawOffset += f5 * 0.2F;
        }
        if (flag)
        {
            f2 *= -1F;
        }
        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        for (; renderYawOffset - prevRenderYawOffset < -180F; prevRenderYawOffset -= 360F) { }
        for (; renderYawOffset - prevRenderYawOffset >= 180F; prevRenderYawOffset += 360F) { }
        for (; rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        field_9360_w += f2;
    }

    protected void setSize(float f, float f1)
    {
        super.setSize(f, f1);
    }

    public void heal(int i)
    {
        if (health <= 0)
        {
            return;
        }
        health += i;
        if (health > getMaxHealth())
        {
            health = getMaxHealth();
        }
        heartsLife = heartsHalvesLife / 2;
    }

    public abstract int getMaxHealth();

    public int getEntityHealth()
    {
        return health;
    }

    public void setEntityHealth(int i)
    {
        health = i;
        if (i > getMaxHealth())
        {
            i = getMaxHealth();
        }
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if (worldObj.isRemote)
        {
            return false;
        }
        entityAge = 0;
        if (health <= 0)
        {
            return false;
        }
        if (damagesource.fireDamage() && isPotionActive(Potion.fireResistance))
        {
            return false;
        }
        field_704_R = 1.5F;
        boolean flag = true;
        if ((float)heartsLife > (float)heartsHalvesLife / 2.0F)
        {
            if (i <= naturalArmorRating)
            {
                return false;
            }
            damageEntity(damagesource, i - naturalArmorRating);
            naturalArmorRating = i;
            flag = false;
        }
        else
        {
            naturalArmorRating = i;
            prevHealth = health;
            heartsLife = heartsHalvesLife;
            damageEntity(damagesource, i);
            hurtTime = maxHurtTime = 10;
        }
        attackedAtYaw = 0.0F;
        Entity entity = damagesource.getEntity();
        if (entity != null)
        {
            if (entity instanceof EntityPlayer)
            {
                recentlyHit = 60;
                attackingPlayer = (EntityPlayer)entity;
            }
            else if (entity instanceof EntityWolf)
            {
                EntityWolf entitywolf = (EntityWolf)entity;
                if (entitywolf.isTamed())
                {
                    recentlyHit = 60;
                    attackingPlayer = null;
                }
            }
        }
        if (flag)
        {
            worldObj.setEntityState(this, (byte)2);
            setBeenAttacked();
            if (entity != null)
            {
                double d = entity.posX - posX;
                double d1;
                for (d1 = entity.posZ - posZ; d * d + d1 * d1 < 0.0001D; d1 = (Math.random() - Math.random()) * 0.01D)
                {
                    d = (Math.random() - Math.random()) * 0.01D;
                }

                attackedAtYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - rotationYaw;
                knockBack(entity, i, d, d1);
            }
            else
            {
                attackedAtYaw = (int)(Math.random() * 2D) * 180;
            }
        }
        if (health <= 0)
        {
            if (flag)
            {
                worldObj.playSoundAtEntity(this, getDeathSound(), getSoundVolume(), getSoundPitch());
            }
            onDeath(damagesource);
        }
        else if (flag)
        {
            worldObj.playSoundAtEntity(this, getHurtSound(), getSoundVolume(), getSoundPitch());
        }
        return true;
    }

    private float getSoundPitch()
    {
        if (isChild())
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F;
        }
        else
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
        }
    }

    public void performHurtAnimation()
    {
        hurtTime = maxHurtTime = 10;
        attackedAtYaw = 0.0F;
    }

    public int getTotalArmorValue()
    {
        return 0;
    }

    protected void func_40125_g(int i)
    {
    }

    protected int applyArmorCalculations(DamageSource damagesource, int i)
    {
        if (!damagesource.isUnblockable())
        {
            int j = 25 - getTotalArmorValue();
            int k = i * j + carryoverDamage;
            func_40125_g(i);
            i = k / 25;
            carryoverDamage = k % 25;
        }
        return i;
    }

    protected int applyPotionDamageCalculations(DamageSource damagesource, int i)
    {
        if (isPotionActive(Potion.resistance))
        {
            int j = (getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
            int k = 25 - j;
            int l = i * k + carryoverDamage;
            i = l / 25;
            carryoverDamage = l % 25;
        }
        return i;
    }

    protected void damageEntity(DamageSource damagesource, int i)
    {
        i = applyArmorCalculations(damagesource, i);
        i = applyPotionDamageCalculations(damagesource, i);
        health -= i;
    }

    protected float getSoundVolume()
    {
        return 1.0F;
    }

    protected String getLivingSound()
    {
        return null;
    }

    protected String getHurtSound()
    {
        return "damage.hurtflesh";
    }

    protected String getDeathSound()
    {
        return "damage.hurtflesh";
    }

    public void knockBack(Entity entity, int i, double d, double d1)
    {
        isAirBorne = true;
        float f = MathHelper.sqrt_double(d * d + d1 * d1);
        float f1 = 0.4F;
        motionX /= 2D;
        motionY /= 2D;
        motionZ /= 2D;
        motionX -= (d / (double)f) * (double)f1;
        motionY += f1;
        motionZ -= (d1 / (double)f) * (double)f1;
        if (motionY > 0.40000000596046448D)
        {
            motionY = 0.40000000596046448D;
        }
    }

    public void onDeath(DamageSource damagesource)
    {
        Entity entity = damagesource.getEntity();
        if (scoreValue >= 0 && entity != null)
        {
            entity.addToPlayerScore(this, scoreValue);
        }
        if (entity != null)
        {
            entity.onKillEntity(this);
        }
        unused_flag = true;
        if (!worldObj.isRemote)
        {
            int i = 0;
            if (entity instanceof EntityPlayer)
            {
                i = EnchantmentHelper.getLootingModifier(((EntityPlayer)entity).inventory);
            }
            if (!isChild())
            {
                dropFewItems(recentlyHit > 0, i);
            }
        }
        worldObj.setEntityState(this, (byte)3);
    }

    protected void dropFewItems(boolean flag, int i)
    {
        int j = getDropItemId();
        if (j > 0)
        {
            int k = rand.nextInt(3);
            if (i > 0)
            {
                k += rand.nextInt(i + 1);
            }
            for (int l = 0; l < k; l++)
            {
                dropItem(j, 1);
            }
        }
    }

    protected int getDropItemId()
    {
        return 0;
    }

    protected void fall(float f)
    {
        super.fall(f);
        int i = (int)Math.ceil(f - 3F);
        if (i > 0)
        {
            if (i > 4)
            {
                worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, 1.0F);
            }
            else
            {
                worldObj.playSoundAtEntity(this, "damage.fallsmall", 1.0F, 1.0F);
            }
            attackEntityFrom(DamageSource.fall, i);
            int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset), MathHelper.floor_double(posZ));
            if (j > 0)
            {
                StepSound stepsound = Block.blocksList[j].stepSound;
                worldObj.playSoundAtEntity(this, stepsound.getStepSound(), stepsound.getVolume() * 0.5F, stepsound.getPitch() * 0.75F);
            }
        }
    }

    public void moveEntityWithHeading(float f, float f1)
    {
        if (isInWater())
        {
            double d = posY;
            moveFlying(f, f1, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.80000001192092896D;
            motionY *= 0.80000001192092896D;
            motionZ *= 0.80000001192092896D;
            motionY -= 0.02D;
            if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, ((motionY + 0.60000002384185791D) - posY) + d, motionZ))
            {
                motionY = 0.30000001192092896D;
            }
        }
        else if (handleLavaMovement())
        {
            double d1 = posY;
            moveFlying(f, f1, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
            motionY -= 0.02D;
            if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, ((motionY + 0.60000002384185791D) - posY) + d1, motionZ))
            {
                motionY = 0.30000001192092896D;
            }
        }
        else
        {
            float f2 = 0.91F;
            if (onGround)
            {
                f2 = 0.5460001F;
                int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
                if (i > 0)
                {
                    f2 = Block.blocksList[i].slipperiness * 0.91F;
                }
            }
            float f3 = 0.1627714F / (f2 * f2 * f2);
            float f4 = onGround ? landMovementFactor * f3 : jumpMovementFactor;
            moveFlying(f, f1, f4);
            f2 = 0.91F;
            if (onGround)
            {
                f2 = 0.5460001F;
                int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
                if (j > 0)
                {
                    f2 = Block.blocksList[j].slipperiness * 0.91F;
                }
            }
            if (isOnLadder())
            {
                float f5 = 0.15F;
                if (motionX < (double)(-f5))
                {
                    motionX = -f5;
                }
                if (motionX > (double)f5)
                {
                    motionX = f5;
                }
                if (motionZ < (double)(-f5))
                {
                    motionZ = -f5;
                }
                if (motionZ > (double)f5)
                {
                    motionZ = f5;
                }
                fallDistance = 0.0F;
                if (motionY < -0.14999999999999999D)
                {
                    motionY = -0.14999999999999999D;
                }
                if (isSneaking() && motionY < 0.0D)
                {
                    motionY = 0.0D;
                }
            }
            moveEntity(motionX, motionY, motionZ);
            if (isCollidedHorizontally && isOnLadder())
            {
                motionY = 0.20000000000000001D;
            }
            motionY -= 0.080000000000000002D;
            motionY *= 0.98000001907348633D;
            motionX *= f2;
            motionZ *= f2;
        }
        field_705_Q = field_704_R;
        double d2 = posX - prevPosX;
        double d3 = posZ - prevPosZ;
        float f6 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4F;
        if (f6 > 1.0F)
        {
            f6 = 1.0F;
        }
        field_704_R += (f6 - field_704_R) * 0.4F;
        field_703_S += field_704_R;
    }

    public boolean isOnLadder()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        return worldObj.getBlockId(i, j, k) == Block.ladder.blockID;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("Health", (short)health);
        nbttagcompound.setShort("HurtTime", (short)hurtTime);
        nbttagcompound.setShort("DeathTime", (short)deathTime);
        nbttagcompound.setShort("AttackTime", (short)attackTime);
        if (!activePotionsMap.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();
            NBTTagCompound nbttagcompound1;
            for (Iterator iterator = activePotionsMap.values().iterator(); iterator.hasNext(); nbttaglist.setTag(nbttagcompound1))
            {
                PotionEffect potioneffect = (PotionEffect)iterator.next();
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Id", (byte)potioneffect.getPotionID());
                nbttagcompound1.setByte("Amplifier", (byte)potioneffect.getAmplifier());
                nbttagcompound1.setInteger("Duration", potioneffect.getDuration());
            }

            nbttagcompound.setTag("ActiveEffects", nbttaglist);
        }
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        health = nbttagcompound.getShort("Health");
        if (!nbttagcompound.hasKey("Health"))
        {
            health = getMaxHealth();
        }
        hurtTime = nbttagcompound.getShort("HurtTime");
        deathTime = nbttagcompound.getShort("DeathTime");
        attackTime = nbttagcompound.getShort("AttackTime");
        if (nbttagcompound.hasKey("ActiveEffects"))
        {
            NBTTagList nbttaglist = nbttagcompound.getTagList("ActiveEffects");
            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
                byte byte0 = nbttagcompound1.getByte("Id");
                byte byte1 = nbttagcompound1.getByte("Amplifier");
                int j = nbttagcompound1.getInteger("Duration");
                activePotionsMap.put(Integer.valueOf(byte0), new PotionEffect(byte0, j, byte1));
            }
        }
    }

    public boolean isEntityAlive()
    {
        return !isDead && health > 0;
    }

    public boolean canBreatheUnderwater()
    {
        return false;
    }

    public void setMoveForward(float f)
    {
        moveForward = f;
    }

    public void setIsJumping(boolean flag)
    {
        isJumping = flag;
    }

    public float getMoveSpeed()
    {
        return moveSpeed;
    }

    public void onLivingUpdate()
    {
        if (field_39003_d > 0)
        {
            field_39003_d--;
        }
        if (newPosRotationIncrements > 0)
        {
            double d = posX + (newPosX - posX) / (double)newPosRotationIncrements;
            double d1 = posY + (newPosY - posY) / (double)newPosRotationIncrements;
            double d2 = posZ + (newPosZ - posZ) / (double)newPosRotationIncrements;
            double d3;
            for (d3 = newRotationYaw - (double)rotationYaw; d3 < -180D; d3 += 360D) { }
            for (; d3 >= 180D; d3 -= 360D) { }
            rotationYaw += d3 / (double)newPosRotationIncrements;
            rotationPitch += (newRotationPitch - (double)rotationPitch) / (double)newPosRotationIncrements;
            newPosRotationIncrements--;
            setPosition(d, d1, d2);
            setRotation(rotationYaw, rotationPitch);
            List list1 = worldObj.getCollidingBoundingBoxes(this, boundingBox.contract(0.03125D, 0.0D, 0.03125D));
            if (list1.size() > 0)
            {
                double d4 = 0.0D;
                for (int j = 0; j < list1.size(); j++)
                {
                    AxisAlignedBB axisalignedbb = (AxisAlignedBB)list1.get(j);
                    if (axisalignedbb.maxY > d4)
                    {
                        d4 = axisalignedbb.maxY;
                    }
                }

                d1 += d4 - boundingBox.minY;
                setPosition(d, d1, d2);
            }
        }
        Profiler.startSection("ai");
        if (isMovementBlocked())
        {
            isJumping = false;
            moveStrafing = 0.0F;
            moveForward = 0.0F;
            randomYawVelocity = 0.0F;
        }
        else if (func_44001_ad())
        {
            if (isAIEnabled())
            {
                updateAITasks();
            }
            else
            {
                updateEntityActionState();
                field_46015_bf = rotationYaw;
            }
        }
        Profiler.endSection();
        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();
        if (isJumping)
        {
            if (flag)
            {
                motionY += 0.039999999105930328D;
            }
            else if (flag1)
            {
                motionY += 0.039999999105930328D;
            }
            else if (onGround && field_39003_d == 0)
            {
                jump();
                field_39003_d = 10;
            }
        }
        else
        {
            field_39003_d = 0;
        }
        moveStrafing *= 0.98F;
        moveForward *= 0.98F;
        randomYawVelocity *= 0.9F;
        float f = landMovementFactor;
        landMovementFactor *= getSpeedModifier();
        moveEntityWithHeading(moveStrafing, moveForward);
        landMovementFactor = f;
        Profiler.startSection("push");
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        if (list != null && list.size() > 0)
        {
            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);
                if (entity.canBePushed())
                {
                    entity.applyEntityCollision(this);
                }
            }
        }
        Profiler.endSection();
    }

    protected boolean isAIEnabled()
    {
        return false;
    }

    protected boolean func_44001_ad()
    {
        return !worldObj.isRemote;
    }

    protected boolean isMovementBlocked()
    {
        return health <= 0;
    }

    public boolean isBlocking()
    {
        return false;
    }

    protected void jump()
    {
        motionY = 0.41999998688697815D;
        if (isPotionActive(Potion.jump))
        {
            motionY += (float)(getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }
        if (isSprinting())
        {
            float f = rotationYaw * 0.01745329F;
            motionX -= MathHelper.sin(f) * 0.2F;
            motionZ += MathHelper.cos(f) * 0.2F;
        }
        isAirBorne = true;
    }

    protected boolean canDespawn()
    {
        return true;
    }

    protected void despawnEntity()
    {
        EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, -1D);
        if (entityplayer != null)
        {
            double d = ((Entity) (entityplayer)).posX - posX;
            double d1 = ((Entity) (entityplayer)).posY - posY;
            double d2 = ((Entity) (entityplayer)).posZ - posZ;
            double d3 = d * d + d1 * d1 + d2 * d2;
            if (canDespawn() && d3 > 16384D)
            {
                setEntityDead();
            }
            if (entityAge > 600 && rand.nextInt(800) == 0 && d3 > 1024D && canDespawn())
            {
                setEntityDead();
            }
            else if (d3 < 1024D)
            {
                entityAge = 0;
            }
        }
    }

    protected void updateAITasks()
    {
        entityAge++;
        despawnEntity();
        if (entityLivingToAttack != null && !entityLivingToAttack.isEntityAlive())
        {
            entityLivingToAttack = null;
        }
        tasks.onUpdateTasks();
        navigation.onUpdateNavigation();
        moveHelper.onUpdateMoveHelper();
        lookHelper.func_46142_a();
        jumpHelper.doJump();
    }

    protected void updateEntityActionState()
    {
        entityAge++;
        EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, -1D);
        despawnEntity();
        moveStrafing = 0.0F;
        moveForward = 0.0F;
        float f = 8F;
        if (rand.nextFloat() < 0.02F)
        {
            EntityPlayer entityplayer1 = worldObj.getClosestPlayerToEntity(this, f);
            if (entityplayer1 != null)
            {
                currentTarget = entityplayer1;
                numTicksToChaseTarget = 10 + rand.nextInt(20);
            }
            else
            {
                randomYawVelocity = (rand.nextFloat() - 0.5F) * 20F;
            }
        }
        if (currentTarget != null)
        {
            faceEntity(currentTarget, 10F, getVerticalFaceSpeed());
            if (numTicksToChaseTarget-- <= 0 || currentTarget.isDead || currentTarget.getDistanceSqToEntity(this) > (double)(f * f))
            {
                currentTarget = null;
            }
        }
        else
        {
            if (rand.nextFloat() < 0.05F)
            {
                randomYawVelocity = (rand.nextFloat() - 0.5F) * 20F;
            }
            rotationYaw += randomYawVelocity;
            rotationPitch = defaultPitch;
        }
        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();
        if (flag || flag1)
        {
            isJumping = rand.nextFloat() < 0.8F;
        }
    }

    public int getVerticalFaceSpeed()
    {
        return 40;
    }

    public void faceEntity(Entity entity, float f, float f1)
    {
        double d = entity.posX - posX;
        double d2 = entity.posZ - posZ;
        double d1;
        if (entity instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)entity;
            d1 = (posY + (double)getEyeHeight()) - (entityliving.posY + (double)entityliving.getEyeHeight());
        }
        else
        {
            d1 = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2D - (posY + (double)getEyeHeight());
        }
        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f2 = (float)((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
        float f3 = (float)(-((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D));
        rotationPitch = -updateRotation(rotationPitch, f3, f1);
        rotationYaw = updateRotation(rotationYaw, f2, f);
    }

    public boolean hasCurrentTarget()
    {
        return currentTarget != null;
    }

    public Entity getCurrentTarget()
    {
        return currentTarget;
    }

    private float updateRotation(float f, float f1, float f2)
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

    public void onEntityDeath()
    {
    }

    public boolean getCanSpawnHere()
    {
        return worldObj.checkIfAABBIsClear(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.getIsAnyLiquid(boundingBox);
    }

    protected void kill()
    {
        attackEntityFrom(DamageSource.outOfWorld, 4);
    }

    public float getSwingProgress(float f)
    {
        float f1 = swingProgress - prevSwingProgress;
        if (f1 < 0.0F)
        {
            f1++;
        }
        return prevSwingProgress + f1 * f;
    }

    public Vec3D getPosition(float f)
    {
        if (f == 1.0F)
        {
            return Vec3D.createVector(posX, posY, posZ);
        }
        else
        {
            double d = prevPosX + (posX - prevPosX) * (double)f;
            double d1 = prevPosY + (posY - prevPosY) * (double)f;
            double d2 = prevPosZ + (posZ - prevPosZ) * (double)f;
            return Vec3D.createVector(d, d1, d2);
        }
    }

    public Vec3D getLookVec()
    {
        return getLook(1.0F);
    }

    public Vec3D getLook(float f)
    {
        if (f == 1.0F)
        {
            float f1 = MathHelper.cos(-rotationYaw * 0.01745329F - 3.141593F);
            float f3 = MathHelper.sin(-rotationYaw * 0.01745329F - 3.141593F);
            float f5 = -MathHelper.cos(-rotationPitch * 0.01745329F);
            float f7 = MathHelper.sin(-rotationPitch * 0.01745329F);
            return Vec3D.createVector(f3 * f5, f7, f1 * f5);
        }
        else
        {
            float f2 = prevRotationPitch + (rotationPitch - prevRotationPitch) * f;
            float f4 = prevRotationYaw + (rotationYaw - prevRotationYaw) * f;
            float f6 = MathHelper.cos(-f4 * 0.01745329F - 3.141593F);
            float f8 = MathHelper.sin(-f4 * 0.01745329F - 3.141593F);
            float f9 = -MathHelper.cos(-f2 * 0.01745329F);
            float f10 = MathHelper.sin(-f2 * 0.01745329F);
            return Vec3D.createVector(f8 * f9, f10, f6 * f9);
        }
    }

    public float func_35159_aC()
    {
        return 1.0F;
    }

    public MovingObjectPosition rayTrace(double d, float f)
    {
        Vec3D vec3d = getPosition(f);
        Vec3D vec3d1 = getLook(f);
        Vec3D vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
        return worldObj.rayTraceBlocks(vec3d, vec3d2);
    }

    public int getMaxSpawnedInChunk()
    {
        return 4;
    }

    public ItemStack getHeldItem()
    {
        return null;
    }

    public void handleHealthUpdate(byte byte0)
    {
        if (byte0 == 2)
        {
            field_704_R = 1.5F;
            heartsLife = heartsHalvesLife;
            hurtTime = maxHurtTime = 10;
            attackedAtYaw = 0.0F;
            worldObj.playSoundAtEntity(this, getHurtSound(), getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            attackEntityFrom(DamageSource.generic, 0);
        }
        else if (byte0 == 3)
        {
            worldObj.playSoundAtEntity(this, getDeathSound(), getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            health = 0;
            onDeath(DamageSource.generic);
        }
        else
        {
            super.handleHealthUpdate(byte0);
        }
    }

    public boolean isPlayerSleeping()
    {
        return false;
    }

    public int getItemIcon(ItemStack itemstack, int i)
    {
        return itemstack.getIconIndex();
    }

    protected void updatePotionEffects()
    {
        Iterator iterator = activePotionsMap.keySet().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            Integer integer = (Integer)iterator.next();
            PotionEffect potioneffect = (PotionEffect)activePotionsMap.get(integer);
            if (!potioneffect.onUpdate(this) && !worldObj.isRemote)
            {
                iterator.remove();
                onFinishedPotionEffect(potioneffect);
            }
        }
        while (true);
        if (field_39001_b)
        {
            if (!worldObj.isRemote)
            {
                if (!activePotionsMap.isEmpty())
                {
                    int i = PotionHelper.func_40354_a(activePotionsMap.values());
                    dataWatcher.updateObject(8, Integer.valueOf(i));
                }
                else
                {
                    dataWatcher.updateObject(8, Integer.valueOf(0));
                }
            }
            field_39001_b = false;
        }
        if (rand.nextBoolean())
        {
            int j = dataWatcher.getWatchableObjectInt(8);
            if (j > 0)
            {
                double d = (double)(j >> 16 & 0xff) / 255D;
                double d1 = (double)(j >> 8 & 0xff) / 255D;
                double d2 = (double)(j >> 0 & 0xff) / 255D;
                worldObj.spawnParticle("mobSpell", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - (double)yOffset, posZ + (rand.nextDouble() - 0.5D) * (double)width, d, d1, d2);
            }
        }
    }

    public void clearActivePotions()
    {
        Iterator iterator = activePotionsMap.keySet().iterator();
        do
        {
            if (!iterator.hasNext())
            {
                break;
            }
            Integer integer = (Integer)iterator.next();
            PotionEffect potioneffect = (PotionEffect)activePotionsMap.get(integer);
            if (!worldObj.isRemote)
            {
                iterator.remove();
                onFinishedPotionEffect(potioneffect);
            }
        }
        while (true);
    }

    public Collection getActivePotionEffects()
    {
        return activePotionsMap.values();
    }

    public boolean isPotionActive(Potion potion)
    {
        return activePotionsMap.containsKey(Integer.valueOf(potion.id));
    }

    public PotionEffect getActivePotionEffect(Potion potion)
    {
        return (PotionEffect)activePotionsMap.get(Integer.valueOf(potion.id));
    }

    public void addPotionEffect(PotionEffect potioneffect)
    {
        if (!func_40126_a(potioneffect))
        {
            return;
        }
        if (activePotionsMap.containsKey(Integer.valueOf(potioneffect.getPotionID())))
        {
            ((PotionEffect)activePotionsMap.get(Integer.valueOf(potioneffect.getPotionID()))).combine(potioneffect);
            onChangedPotionEffect((PotionEffect)activePotionsMap.get(Integer.valueOf(potioneffect.getPotionID())));
        }
        else
        {
            activePotionsMap.put(Integer.valueOf(potioneffect.getPotionID()), potioneffect);
            onNewPotionEffect(potioneffect);
        }
    }

    public boolean func_40126_a(PotionEffect potioneffect)
    {
        if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
        {
            int i = potioneffect.getPotionID();
            if (i == Potion.regeneration.id || i == Potion.poison.id)
            {
                return false;
            }
        }
        return true;
    }

    public boolean isEntityUndead()
    {
        return getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }

    public void removePotionEffect(int i)
    {
        activePotionsMap.remove(Integer.valueOf(i));
    }

    protected void onNewPotionEffect(PotionEffect potioneffect)
    {
        field_39001_b = true;
    }

    protected void onChangedPotionEffect(PotionEffect potioneffect)
    {
        field_39001_b = true;
    }

    protected void onFinishedPotionEffect(PotionEffect potioneffect)
    {
        field_39001_b = true;
    }

    protected float getSpeedModifier()
    {
        float f = 1.0F;
        if (isPotionActive(Potion.moveSpeed))
        {
            f *= 1.0F + 0.2F * (float)(getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        if (isPotionActive(Potion.moveSlowdown))
        {
            f *= 1.0F - 0.15F * (float)(getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
        }
        return f;
    }

    public void setPositionAndUpdate(double d, double d1, double d2)
    {
        setLocationAndAngles(d, d1, d2, rotationYaw, rotationPitch);
    }

    public boolean isChild()
    {
        return false;
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public void func_41005_b(ItemStack itemstack)
    {
        worldObj.playSoundAtEntity(this, "random.break", 0.8F, 0.8F + worldObj.rand.nextFloat() * 0.4F);
        for (int i = 0; i < 5; i++)
        {
            Vec3D vec3d = Vec3D.createVector(((double)rand.nextFloat() - 0.5D) * 0.10000000000000001D, Math.random() * 0.10000000000000001D + 0.10000000000000001D, 0.0D);
            vec3d.rotateAroundX((-rotationPitch * 3.141593F) / 180F);
            vec3d.rotateAroundY((-rotationYaw * 3.141593F) / 180F);
            Vec3D vec3d1 = Vec3D.createVector(((double)rand.nextFloat() - 0.5D) * 0.29999999999999999D, (double)(-rand.nextFloat()) * 0.59999999999999998D - 0.29999999999999999D, 0.59999999999999998D);
            vec3d1.rotateAroundX((-rotationPitch * 3.141593F) / 180F);
            vec3d1.rotateAroundY((-rotationYaw * 3.141593F) / 180F);
            vec3d1 = vec3d1.addVector(posX, posY + (double)getEyeHeight(), posZ);
            worldObj.spawnParticle((new StringBuilder()).append("iconcrack_").append(itemstack.getItem().shiftedIndex).toString(), vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.050000000000000003D, vec3d.zCoord);
        }
    }
}
