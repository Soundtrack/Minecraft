package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
public class EntityPlayerSP extends EntityPlayer
{
    public MovementInput movementInput;
    protected Minecraft mc;
    protected int sprintToggleTimer;
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private MouseFilter field_21903_bJ;
    private MouseFilter field_21904_bK;
    private MouseFilter field_21902_bL;

    public EntityPlayerSP(Minecraft minecraft, World world, Session session, int i)
    {
        super(world);
        sprintToggleTimer = 0;
        sprintingTicksLeft = 0;
        field_21903_bJ = new MouseFilter();
        field_21904_bK = new MouseFilter();
        field_21902_bL = new MouseFilter();
        mc = minecraft;
        dimension = i;
        if (session != null && session.username != null && session.username.length() > 0)
        {
            skinUrl = (new StringBuilder()).append("http://s3.amazonaws.com/MinecraftSkins/").append(session.username).append(".png").toString();
        }
        username = session.username;
    }

    public void moveEntity(double d, double d1, double d2)
    {
        super.moveEntity(d, d1, d2);
    }

    public void updateEntityActionState()
    {
        super.updateEntityActionState();
        moveStrafing = movementInput.moveStrafe;
        moveForward = movementInput.moveForward;
        isJumping = movementInput.jump;
        prevRenderArmYaw = renderArmYaw;
        prevRenderArmPitch = renderArmPitch;
        renderArmPitch += (double)(rotationPitch - renderArmPitch) * 0.5D;
        renderArmYaw += (double)(rotationYaw - renderArmYaw) * 0.5D;
    }

    protected boolean func_44001_ad()
    {
        return true;
    }

    public void onLivingUpdate()
    {
        if (sprintingTicksLeft > 0)
        {
            sprintingTicksLeft--;
            if (sprintingTicksLeft == 0)
            {
                setSprinting(false);
            }
        }
        if (sprintToggleTimer > 0)
        {
            sprintToggleTimer--;
        }
        if (mc.playerController.func_35643_e())
        {
            posX = posZ = 0.5D;
            posX = 0.0D;
            posZ = 0.0D;
            rotationYaw = (float)ticksExisted / 12F;
            rotationPitch = 10F;
            posY = 68.5D;
            return;
        }
        if (!mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory))
        {
            mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
        }
        prevTimeInPortal = timeInPortal;
        if (inPortal)
        {
            if (!worldObj.multiplayerWorld && ridingEntity != null)
            {
                mountEntity(null);
            }
            if (mc.currentScreen != null)
            {
                mc.displayGuiScreen(null);
            }
            if (timeInPortal == 0.0F)
            {
                mc.sndManager.playSoundFX("portal.trigger", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
            }
            timeInPortal += 0.0125F;
            if (timeInPortal >= 1.0F)
            {
                timeInPortal = 1.0F;
                if (!worldObj.multiplayerWorld)
                {
                    timeUntilPortal = 10;
                    mc.sndManager.playSoundFX("portal.travel", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                    byte byte0 = 0;
                    if (dimension == -1)
                    {
                        byte0 = 0;
                    }
                    else
                    {
                        byte0 = -1;
                    }
                    mc.usePortal(byte0);
                    triggerAchievement(AchievementList.portal);
                }
            }
            inPortal = false;
        }
        else if (isPotionActive(Potion.confusion) && getActivePotionEffect(Potion.confusion).getDuration() > 60)
        {
            timeInPortal += 0.006666667F;
            if (timeInPortal > 1.0F)
            {
                timeInPortal = 1.0F;
            }
        }
        else
        {
            if (timeInPortal > 0.0F)
            {
                timeInPortal -= 0.05F;
            }
            if (timeInPortal < 0.0F)
            {
                timeInPortal = 0.0F;
            }
        }
        if (timeUntilPortal > 0)
        {
            timeUntilPortal--;
        }
        boolean flag = movementInput.jump;
        float f = 0.8F;
        boolean flag1 = movementInput.moveForward >= f;
        movementInput.updatePlayerMoveState(this);
        if (isUsingItem())
        {
            movementInput.moveStrafe *= 0.2F;
            movementInput.moveForward *= 0.2F;
            sprintToggleTimer = 0;
        }
        if (movementInput.sneak && ySize < 0.2F)
        {
            ySize = 0.2F;
        }
        pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ + (double)width * 0.34999999999999998D);
        pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ - (double)width * 0.34999999999999998D);
        pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ - (double)width * 0.34999999999999998D);
        pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ + (double)width * 0.34999999999999998D);
        boolean flag2 = (float)getFoodStats().getFoodLevel() > 6F;
        if (onGround && !flag1 && movementInput.moveForward >= f && !isSprinting() && flag2 && !isUsingItem() && !isPotionActive(Potion.blindness))
        {
            if (sprintToggleTimer == 0)
            {
                sprintToggleTimer = 7;
            }
            else
            {
                setSprinting(true);
                sprintToggleTimer = 0;
            }
        }
        if (isSneaking())
        {
            sprintToggleTimer = 0;
        }
        if (isSprinting() && (movementInput.moveForward < f || isCollidedHorizontally || !flag2))
        {
            setSprinting(false);
        }
        if (capabilities.allowFlying && !flag && movementInput.jump)
        {
            if (flyToggleTimer == 0)
            {
                flyToggleTimer = 7;
            }
            else
            {
                capabilities.isFlying = !capabilities.isFlying;
                flyToggleTimer = 0;
            }
        }
        if (capabilities.isFlying)
        {
            if (movementInput.sneak)
            {
                motionY -= 0.14999999999999999D;
            }
            if (movementInput.jump)
            {
                motionY += 0.14999999999999999D;
            }
        }
        super.onLivingUpdate();
        if (onGround && capabilities.isFlying)
        {
            capabilities.isFlying = false;
        }
    }

    public void func_40182_b(int i)
    {
        if (!worldObj.multiplayerWorld)
        {
            if (dimension == 1 && i == 1)
            {
                triggerAchievement(AchievementList.theEnd2);
                mc.displayGuiScreen(new GuiWinGame());
            }
            else
            {
                triggerAchievement(AchievementList.theEnd);
                mc.sndManager.playSoundFX("portal.travel", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
                mc.usePortal(1);
            }
        }
    }

    public float getFOVMultiplier()
    {
        float f = 1.0F;
        if (capabilities.isFlying)
        {
            f *= 1.1F;
        }
        f *= ((landMovementFactor * getSpeedModifier()) / speedOnGround + 1.0F) / 2.0F;
        if (isUsingItem() && getItemInUse().itemID == Item.bow.shiftedIndex)
        {
            int i = getItemInUseDuration();
            float f1 = (float)i / 20F;
            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }
            else
            {
                f1 *= f1;
            }
            f *= 1.0F - f1 * 0.15F;
        }
        return f;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Score", score);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        score = nbttagcompound.getInteger("Score");
    }

    public void closeScreen()
    {
        super.closeScreen();
        mc.displayGuiScreen(null);
    }

    public void displayGUIEditSign(TileEntitySign tileentitysign)
    {
        mc.displayGuiScreen(new GuiEditSign(tileentitysign));
    }

    public void displayGUIChest(IInventory iinventory)
    {
        mc.displayGuiScreen(new GuiChest(inventory, iinventory));
    }

    public void displayWorkbenchGUI(int i, int j, int k)
    {
        mc.displayGuiScreen(new GuiCrafting(inventory, worldObj, i, j, k));
    }

    public void displayGUIEnchantment(int i, int j, int k)
    {
        mc.displayGuiScreen(new GuiEnchantment(inventory, worldObj, i, j, k));
    }

    public void displayGUIFurnace(TileEntityFurnace tileentityfurnace)
    {
        mc.displayGuiScreen(new GuiFurnace(inventory, tileentityfurnace));
    }

    public void displayGUIBrewingStand(TileEntityBrewingStand tileentitybrewingstand)
    {
        mc.displayGuiScreen(new GuiBrewingStand(inventory, tileentitybrewingstand));
    }

    public void displayGUIDispenser(TileEntityDispenser tileentitydispenser)
    {
        mc.displayGuiScreen(new GuiDispenser(inventory, tileentitydispenser));
    }

    public void onCriticalHit(Entity entity)
    {
        mc.effectRenderer.addEffect(new EntityCrit2FX(mc.theWorld, entity));
    }

    public void func_40183_c(Entity entity)
    {
        EntityCrit2FX entitycrit2fx = new EntityCrit2FX(mc.theWorld, entity, "magicCrit");
        mc.effectRenderer.addEffect(entitycrit2fx);
    }

    public void onItemPickup(Entity entity, int i)
    {
        mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, entity, this, -0.5F));
    }

    public void sendChatMessage(String s)
    {
    }

    public boolean isSneaking()
    {
        return movementInput.sneak && !sleeping;
    }

    public void setHealth(int i)
    {
        int j = getEntityHealth() - i;
        if (j <= 0)
        {
            setEntityHealth(i);
            if (j < 0)
            {
                heartsLife = heartsHalvesLife / 2;
            }
        }
        else
        {
            naturalArmorRating = j;
            setEntityHealth(getEntityHealth());
            heartsLife = heartsHalvesLife;
            damageEntity(DamageSource.generic, j);
            hurtTime = maxHurtTime = 10;
        }
    }

    public void respawnPlayer()
    {
        mc.respawn(false, 0, false);
    }

    public void func_6420_o()
    {
    }

    public void addChatMessage(String s)
    {
        mc.ingameGUI.addChatMessageTranslate(s);
    }

    public void addStat(StatBase statbase, int i)
    {
        if (statbase == null)
        {
            return;
        }
        if (statbase.isAchievement())
        {
            Achievement achievement = (Achievement)statbase;
            if (achievement.parentAchievement == null || mc.statFileWriter.hasAchievementUnlocked(achievement.parentAchievement))
            {
                if (!mc.statFileWriter.hasAchievementUnlocked(achievement))
                {
                    mc.guiAchievement.queueTakenAchievement(achievement);
                }
                mc.statFileWriter.readStat(statbase, i);
            }
        }
        else
        {
            mc.statFileWriter.readStat(statbase, i);
        }
    }

    private boolean isBlockTranslucent(int i, int j, int k)
    {
        return worldObj.isBlockNormalCube(i, j, k);
    }

    protected boolean pushOutOfBlocks(double d, double d1, double d2)
    {
        int i = MathHelper.floor_double(d);
        int j = MathHelper.floor_double(d1);
        int k = MathHelper.floor_double(d2);
        double d3 = d - (double)i;
        double d4 = d2 - (double)k;
        if (isBlockTranslucent(i, j, k) || isBlockTranslucent(i, j + 1, k))
        {
            boolean flag = !isBlockTranslucent(i - 1, j, k) && !isBlockTranslucent(i - 1, j + 1, k);
            boolean flag1 = !isBlockTranslucent(i + 1, j, k) && !isBlockTranslucent(i + 1, j + 1, k);
            boolean flag2 = !isBlockTranslucent(i, j, k - 1) && !isBlockTranslucent(i, j + 1, k - 1);
            boolean flag3 = !isBlockTranslucent(i, j, k + 1) && !isBlockTranslucent(i, j + 1, k + 1);
            byte byte0 = -1;
            double d5 = 9999D;
            if (flag && d3 < d5)
            {
                d5 = d3;
                byte0 = 0;
            }
            if (flag1 && 1.0D - d3 < d5)
            {
                d5 = 1.0D - d3;
                byte0 = 1;
            }
            if (flag2 && d4 < d5)
            {
                d5 = d4;
                byte0 = 4;
            }
            if (flag3 && 1.0D - d4 < d5)
            {
                double d6 = 1.0D - d4;
                byte0 = 5;
            }
            float f = 0.1F;
            if (byte0 == 0)
            {
                motionX = -f;
            }
            if (byte0 == 1)
            {
                motionX = f;
            }
            if (byte0 == 4)
            {
                motionZ = -f;
            }
            if (byte0 == 5)
            {
                motionZ = f;
            }
        }
        if(GuiIngame.fly)
        {
        	onGround = false;
        	motionX = 0.0D;
        	motionY = 0.0D;
        	motionZ = 0.0D;
        	if(Keyboard.isKeyDown(57) && mc.inGameHasFocus)
        	{
        		motionY++;
        	}
        	if(Keyboard.isKeyDown(42) && mc.inGameHasFocus)
        	{
        		motionY--;
        	}
        	double d5 = rotationPitch + 90F;
        	double d6 = rotationYaw + 90F;
        	boolean flag4 = Keyboard.isKeyDown(17) && mc.inGameHasFocus;
        	boolean flag5 = Keyboard.isKeyDown(31) && mc.inGameHasFocus;
        	boolean flag6 = Keyboard.isKeyDown(30) && mc.inGameHasFocus;
        	boolean flag7 = Keyboard.isKeyDown(32) && mc.inGameHasFocus;
        	if(flag4)
        	{
        		if(flag6)
        		{
        			d6 -= 45D;
        		}else
                    if(flag7)
                    {
                        d6 += 45D;
                    }
        	}else
                if(flag5)
                {
                    d6 += 180D;
                    if(flag6)
                    {
                        d6 += 45D;
                    }else
                        if(flag7)
                        {
                            d6 -= 45D;
                        }
                }else
                    if(flag6)
                    {
                        d6 -= 90D;
                    }else
                        if(flag7)
                        {
                            d6 += 90D;
                        }
        	if(flag4 || flag6 || flag5 || flag7)
        	{
        		motionX = Math.cos(Math.toRadians(d6));
        		motionZ = Math.sin(Math.toRadians(d6));
        	}
        }
        return false;
    }

    public void setSprinting(boolean flag)
    {
        super.setSprinting(flag);
        if (!flag)
        {
            sprintingTicksLeft = 0;
        }
        else
        {
            sprintingTicksLeft = 600;
        }
    }

    public void setXPStats(float f, int i, int j)
    {
        currentXP = f;
        totalXP = i;
        playerLevel = j;
    }
}
