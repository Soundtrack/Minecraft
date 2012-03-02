package net.minecraft.src;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

public class EntityClientPlayerMP extends EntityPlayerSP
{
    public static ArrayList NameListfriend;
	public NetClientHandler sendQueue;
    private int inventoryUpdateTickCounter;
    private boolean field_21093_bH;
    private double oldPosX;
    private double field_9378_bz;
    private double oldPosY;
    private double oldPosZ;
    private float oldRotationYaw;
    private float oldRotationPitch;
    private boolean field_9382_bF;
    private boolean field_35227_cs;
    private boolean wasSneaking;
    private int field_12242_bI;
	private ArrayList NameListenemy;

    public EntityClientPlayerMP(Minecraft minecraft, World world, Session session, NetClientHandler netclienthandler)
    {
        super(minecraft, world, session, 0);
        inventoryUpdateTickCounter = 0;
        field_21093_bH = false;
        field_9382_bF = false;
        field_35227_cs = false;
        wasSneaking = false;
        field_12242_bI = 0;
        sendQueue = netclienthandler;
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        return false;
    }

    public void heal(int i)
    {
    }

    public void onUpdate()
    {
        if (!worldObj.blockExists(MathHelper.floor_double(posX), worldObj.worldHeight / 2, MathHelper.floor_double(posZ)))
        {
            return;
        }
        else
        {
            super.onUpdate();
            onUpdate2();
            return;
        }
    }

    public void onUpdate2()
    {
    	if(GuiIngame.killaura)
        {
            for(int i = 0; i < mc.theWorld.loadedEntityList.size(); i++)
            {
                if((Entity)mc.theWorld.loadedEntityList.get(i) == this || getDistanceSqToEntity((Entity)mc.theWorld.loadedEntityList.get(i)) >= 25D || !((Entity)mc.theWorld.loadedEntityList.get(i) instanceof EntityLiving))
                {
                    continue;
                }
                sendQueue.addToSendQueue(new Packet19EntityAction(this, 4));
                for(int k = 0; k < 3; k++)
                {
                    mc.playerController.attackEntity(this, (Entity)mc.theWorld.loadedEntityList.get(i));
                }

            }

        }
        if (inventoryUpdateTickCounter++ == 20)
        {
            sendInventoryChanged();
            inventoryUpdateTickCounter = 0;
        }
        boolean flag = isSprinting();
        if (flag != wasSneaking)
        {
            if (flag)
            {
                sendQueue.addToSendQueue(new Packet19EntityAction(this, 4));
            }
            else
            {
                sendQueue.addToSendQueue(new Packet19EntityAction(this, 5));
            }
            wasSneaking = flag;
        }
        boolean flag1 = GuiIngame.sneak;
        if (flag1 != field_35227_cs)
        {
            if (flag1)
            {
                sendQueue.addToSendQueue(new Packet19EntityAction(this, 1));
            }
            else
            {
                sendQueue.addToSendQueue(new Packet19EntityAction(this, 2));
            }
            field_35227_cs = flag1;
        }
        double d = posX - oldPosX;
        double d1 = boundingBox.minY - field_9378_bz;
        double d2 = posY - oldPosY;
        double d3 = posZ - oldPosZ;
        double d4 = rotationYaw - oldRotationYaw;
        double d5 = rotationPitch - oldRotationPitch;
        boolean flag2 = d1 != 0.0D || d2 != 0.0D || d != 0.0D || d3 != 0.0D;
        boolean flag3 = d4 != 0.0D || d5 != 0.0D;
        if (ridingEntity != null)
        {
            if (flag3)
            {
                sendQueue.addToSendQueue(new Packet11PlayerPosition(motionX, -999D, -999D, motionZ, onGround));
            }
            else
            {
                sendQueue.addToSendQueue(new Packet13PlayerLookMove(motionX, -999D, -999D, motionZ, rotationYaw, rotationPitch, onGround));
            }
            flag2 = false;
        }
        else if (flag2 && flag3)
        {
            sendQueue.addToSendQueue(new Packet13PlayerLookMove(posX, boundingBox.minY, posY, posZ, rotationYaw, rotationPitch, onGround));
            field_12242_bI = 0;
        }
        else if (flag2)
        {
            sendQueue.addToSendQueue(new Packet11PlayerPosition(posX, boundingBox.minY, posY, posZ, onGround));
            field_12242_bI = 0;
        }
        else if (flag3)
        {
            sendQueue.addToSendQueue(new Packet12PlayerLook(rotationYaw, rotationPitch, onGround));
            field_12242_bI = 0;
        }
        else
        {
            sendQueue.addToSendQueue(new Packet10Flying(onGround));
            if (field_9382_bF != onGround || field_12242_bI > 200)
            {
                field_12242_bI = 0;
            }
            else
            {
                field_12242_bI++;
            }
        }
        field_9382_bF = onGround;
        if (flag2)
        {
            oldPosX = posX;
            field_9378_bz = boundingBox.minY;
            oldPosY = posY;
            oldPosZ = posZ;
        }
        if (flag3)
        {
            oldRotationYaw = rotationYaw;
            oldRotationPitch = rotationPitch;
        }
    }

    public void dropCurrentItem()
    {
        sendQueue.addToSendQueue(new Packet14BlockDig(4, 0, 0, 0, 0));
    }

    public void sendInventoryChanged()
    {
    }

    protected void joinEntityItemWithWorld(EntityItem entityitem)
    {
    }

    public void sendChatMessage(String s)
    {
    	if(GuiIngame.jump){
        	mc.thePlayer.addChatMessage("Jump Hack On");
        	}else{
        		mc.thePlayer.addChatMessage("Jump Hack Off");
        	}
    	if(s.startsWith(".instant")){
    		try
            {
                String as12[] = s.split(" ");
                Float float5 = new Float(as12[1]);
                GuiIngame.instantSpeed = float5.floatValue();
                mc.thePlayer.addChatMessage((new StringBuilder()).append("\2474Instant Speed changed to: ").append(float5).toString());
            }
            catch(Exception exception9)
            {
                mc.thePlayer.addChatMessage("\247cIncorrect, Usage: .instant [Integer]");
            }
    		return;
    	}
    	if(s.startsWith(".nuker")){
    		try
            {
                String as12[] = s.split(" ");
                Float float5 = new Float(as12[1]);
                PlayerControllerMP.ndis = float5.floatValue();
                mc.thePlayer.addChatMessage((new StringBuilder()).append("\2474Instant Speed changed to: ").append(float5).toString());
            }
            catch(Exception exception9)
            {
                mc.thePlayer.addChatMessage("\247cIncorrect, Usage: .instant [Integer]");
            }
    		return;
    	}
    	if(s.startsWith(".timer")){
    		try
            {
                String as12[] = s.split(" ");
                Float float5 = new Float(as12[1]);
                GuiIngame.timerSpeed = float5.floatValue();
                mc.thePlayer.addChatMessage((new StringBuilder()).append("\2474Timer Speed changed to: ").append(float5).toString());
            }
            catch(Exception exception9)
            {
                mc.thePlayer.addChatMessage("\247cIncorrect, Usage: .timer [Integer]");
            }
    		return;
    	}
    	if(s.startsWith(".friend"))
        {
            try
            {
                String as22[] = s.split(" ");
                String s9 = as22[1];
                String s14 = as22[2];
                if(s9.equals("add"))
                {
                    if(!NameListfriend.contains(s14))
                    {
                        NameListfriend.add(s14);
                        mc.thePlayer.addChatMessage("\247b    Name added successfully");
                    } else
                    {
                        mc.thePlayer.addChatMessage("\247b    NameList already contains said name");
                    }
                    return;
                } else
                if(s9.equals("del"))
                {
                    if(NameListfriend.contains(s14))
                    {
                        NameListfriend.remove(s14);
                        mc.thePlayer.addChatMessage("\247b    Name deleted successfully");
                    } else
                    {
                        mc.thePlayer.addChatMessage("\247b    Namelist doesn't contain said name");
                    }
                } else
                {
                    mc.thePlayer.addChatMessage("Syntax: .friend [add/del] <username>");
                }
                return;
            }
            catch(IndexOutOfBoundsException indexoutofboundsexception)
            {
                mc.thePlayer.addChatMessage("NameList not created.");
            }
            catch(Exception exception21)
            {
                mc.thePlayer.addChatMessage("Error In Handling NameList");
                mc.thePlayer.addChatMessage("Syntax: .friend [add/del] <username>");
            }
            mc.thePlayer.sendChatMessage(".save");
            return;
        } else
        if(s.startsWith(".enemy"))
        {
            try
            {
                String as23[] = s.split(" ");
                String s10 = as23[1];
                String s15 = as23[2];
                if(s10.equals("add"))
                {
                    if(!NameListenemy.contains(s15))
                    {
                        NameListenemy.add(s15);
                        mc.thePlayer.addChatMessage("\247b    Name added successfully");
                    } else
                    {
                        mc.thePlayer.addChatMessage("\247b    NameList already contains said name");
                    }
                    return;
                } else
                if(s10.equals("del"))
                {
                    if(NameListenemy.contains(s15))
                    {
                        NameListenemy.remove(s15);
                        mc.thePlayer.addChatMessage("\247b    Name deleted successfully");
                    } else
                    {
                        mc.thePlayer.addChatMessage("\247b    Namelist doesn't contain said name");
                    }
                } else
                {
                    mc.thePlayer.addChatMessage("Syntax: .enemy [add/del] <username>");
                }
                return;
            }
            catch(IndexOutOfBoundsException indexoutofboundsexception1)
            {
                mc.thePlayer.addChatMessage("NameList not created.");
            }
            catch(Exception exception22)
            {
                mc.thePlayer.addChatMessage("Error In Handling NameList");
                mc.thePlayer.addChatMessage("Syntax: .enemy [add/del] <username>");
            }
            mc.thePlayer.sendChatMessage(".save");
            return;
        }
        sendQueue.addToSendQueue(new Packet3Chat(s));
    }

    public void swingItem()
    {
        super.swingItem();
        sendQueue.addToSendQueue(new Packet18Animation(this, 1));
    }

    public void respawnPlayer()
    {
        sendInventoryChanged();
        sendQueue.addToSendQueue(new Packet9Respawn((byte)dimension, (byte)worldObj.difficultySetting, worldObj.getWorldSeed(), worldObj.getWorldInfo().func_46133_t(), worldObj.worldHeight, 0));
    }

    protected void damageEntity(DamageSource damagesource, int i)
    {
        setEntityHealth(getEntityHealth() - i);
    }

    public void closeScreen()
    {
        sendQueue.addToSendQueue(new Packet101CloseWindow(craftingInventory.windowId));
        inventory.setItemStack(null);
        super.closeScreen();
    }

    public void setHealth(int i)
    {
        if (field_21093_bH)
        {
            super.setHealth(i);
        }
        else
        {
            setEntityHealth(i);
            field_21093_bH = true;
        }
    }

    public void addStat(StatBase statbase, int i)
    {
        if (statbase == null)
        {
            return;
        }
        if (statbase.isIndependent)
        {
            super.addStat(statbase, i);
        }
    }

    public void incrementStat(StatBase statbase, int i)
    {
        if (statbase == null)
        {
            return;
        }
        if (!statbase.isIndependent)
        {
            super.addStat(statbase, i);
        }
    }
}
