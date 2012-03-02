package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerMP extends PlayerController
{
    private int currentBlockX;
    private int currentBlockY;
    private int currentblockZ;
    private float curBlockDamageMP;
    private float prevBlockDamageMP;
    private float stepSoundTickCounter;
    private int blockHitDelay;
    private boolean isHittingBlock;
    private boolean creativeMode;
    private NetClientHandler netClientHandler;
    private int currentPlayerItem;
	public static  float ndis = 1F;

    public PlayerControllerMP(Minecraft minecraft, NetClientHandler netclienthandler)
    {
        super(minecraft);
        currentBlockX = -1;
        currentBlockY = -1;
        currentblockZ = -1;
        curBlockDamageMP = 0.0F;
        prevBlockDamageMP = 0.0F;
        stepSoundTickCounter = 0.0F;
        blockHitDelay = 0;
        isHittingBlock = false;
        currentPlayerItem = 0;
        netClientHandler = netclienthandler;
    }

    public void setCreative(boolean flag)
    {
        creativeMode = flag;
        if (creativeMode)
        {
            PlayerControllerCreative.enableAbilities(mc.thePlayer);
        }
        else
        {
            PlayerControllerCreative.disableAbilities(mc.thePlayer);
        }
    }

    public void flipPlayer(EntityPlayer entityplayer)
    {
        entityplayer.rotationYaw = -180F;
    }

    public boolean shouldDrawHUD()
    {
        return !creativeMode;
    }

    public boolean onPlayerDestroyBlock(int i, int j, int k, int l)
    {
        if (creativeMode)
        {
            return super.onPlayerDestroyBlock(i, j, k, l);
        }
        int i1 = mc.theWorld.getBlockId(i, j, k);
        boolean flag = super.onPlayerDestroyBlock(i, j, k, l);
        ItemStack itemstack = mc.thePlayer.getCurrentEquippedItem();
        if (itemstack != null)
        {
            itemstack.onDestroyBlock(i1, i, j, k, mc.thePlayer);
            if (itemstack.stackSize == 0)
            {
                itemstack.onItemDestroyedByUse(mc.thePlayer);
                mc.thePlayer.destroyCurrentEquippedItem();
            }
        }
        return flag;
    }

    public void clickBlock(int i, int j, int k, int l)
    {
    	if(GuiIngame.nuker)
        {
    		
            float byte0 = ndis ;
            for(float j1 = byte0; j1 > -byte0; j1--)
            {
                for(float k1 = byte0; k1 > -byte0; k1--)
                {
                    for(float l1 = byte0; l1 > -byte0; l1--)
                    {
                        double d = mc.thePlayer.posX + (double)j1;
                        double d1 = mc.thePlayer.posY + (double)k1;
                        double d2 = mc.thePlayer.posZ + (double)l1;
                        int i2 = (int)d;
                        int j2 = (int)d1;
                        int k2 = (int)d2;
                        int l2 = mc.theWorld.getBlockId(i2, j2, k2);
                        Block block = Block.blocksList[l2];
                        if(block != null)
                        {
                            ((EntityClientPlayerMP)mc.thePlayer).sendQueue.addToSendQueue(new Packet14BlockDig(0, i2, j2, k2, 1));
                            ((EntityClientPlayerMP)mc.thePlayer).sendQueue.addToSendQueue(new Packet14BlockDig(2, i2, j2, k2, 1));

                        }
                    }
                }
            }
    		
        }
        if (creativeMode)
        {
            netClientHandler.addToSendQueue(new Packet14BlockDig(0, i, j, k, l));
            PlayerControllerCreative.clickBlockCreative(mc, this, i, j, k, l);
            blockHitDelay = 5;
        }
        else if (!isHittingBlock || i != currentBlockX || j != currentBlockY || k != currentblockZ)
        {
            netClientHandler.addToSendQueue(new Packet14BlockDig(0, i, j, k, l));
            int i1 = mc.theWorld.getBlockId(i, j, k);
            if (i1 > 0 && curBlockDamageMP == 0.0F)
            {
                Block.blocksList[i1].onBlockClicked(mc.theWorld, i, j, k, mc.thePlayer);
            }
            if (i1 > 0 && Block.blocksList[i1].blockStrength(mc.thePlayer) >= 1.0F)
            {
                onPlayerDestroyBlock(i, j, k, l);
            }
            else
            {
                isHittingBlock = true;
                currentBlockX = i;
                currentBlockY = j;
                currentblockZ = k;
                curBlockDamageMP = 0.0F;
                prevBlockDamageMP = 0.0F;
                stepSoundTickCounter = 0.0F;
            }
        }
    }

    public void resetBlockRemoving()
    {
        curBlockDamageMP = 0.0F;
        isHittingBlock = false;
    }

    public void onPlayerDamageBlock(int i, int j, int k, int l)
    {
        syncCurrentPlayItem();
        if (blockHitDelay > 0)
        {
            blockHitDelay--;
            return;
        }
        if (creativeMode)
        {
            blockHitDelay = 5;
            netClientHandler.addToSendQueue(new Packet14BlockDig(0, i, j, k, l));
            PlayerControllerCreative.clickBlockCreative(mc, this, i, j, k, l);
            return;
        }
        if (i == currentBlockX && j == currentBlockY && k == currentblockZ)
        {
            int i1 = mc.theWorld.getBlockId(i, j, k);
            if (i1 == 0)
            {
                isHittingBlock = false;
                return;
            }
            Block block = Block.blocksList[i1];
            if(GuiIngame.instant){
            	curBlockDamageMP += block.blockStrength(mc.thePlayer) * GuiIngame.instantSpeed;
            }else{
            	curBlockDamageMP += block.blockStrength(mc.thePlayer);
            }
            if (stepSoundTickCounter % 4F == 0.0F && block != null)
            {
                mc.sndManager.playSound(block.stepSound.stepSoundDir2(), (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, (block.stepSound.getVolume() + 1.0F) / 8F, block.stepSound.getPitch() * 0.5F);
            }
            stepSoundTickCounter++;
            if (curBlockDamageMP >= 1.0F)
            {
                isHittingBlock = false;
                netClientHandler.addToSendQueue(new Packet14BlockDig(2, i, j, k, l));
                onPlayerDestroyBlock(i, j, k, l);
                curBlockDamageMP = 0.0F;
                prevBlockDamageMP = 0.0F;
                stepSoundTickCounter = 0.0F;
                blockHitDelay = 5;
            }
        }
        else
        {
            clickBlock(i, j, k, l);
        }
    }

    public void setPartialTime(float f)
    {
        if (curBlockDamageMP <= 0.0F)
        {
            mc.ingameGUI.damageGuiPartialTime = 0.0F;
            mc.renderGlobal.damagePartialTime = 0.0F;
        }
        else
        {
            float f1 = prevBlockDamageMP + (curBlockDamageMP - prevBlockDamageMP) * f;
            mc.ingameGUI.damageGuiPartialTime = f1;
            mc.renderGlobal.damagePartialTime = f1;
        }
    }

    public float getBlockReachDistance()
    {
        return !creativeMode ? 4.5F : 5F;
    }

    public void onWorldChange(World world)
    {
        super.onWorldChange(world);
    }

    public void updateController()
    {
        syncCurrentPlayItem();
        prevBlockDamageMP = curBlockDamageMP;
        mc.sndManager.playRandomMusicIfReady();
    }

    private void syncCurrentPlayItem()
    {
        int i = mc.thePlayer.inventory.currentItem;
        if (i != currentPlayerItem)
        {
            currentPlayerItem = i;
            netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(currentPlayerItem));
        }
    }

    public boolean onPlayerRightClick(EntityPlayer entityplayer, World world, ItemStack itemstack, int i, int j, int k, int l)
    {
        syncCurrentPlayItem();
        netClientHandler.addToSendQueue(new Packet15Place(i, j, k, l, entityplayer.inventory.getCurrentItem()));
        int i1 = world.getBlockId(i, j, k);
        if (i1 > 0 && Block.blocksList[i1].blockActivated(world, i, j, k, entityplayer))
        {
            return true;
        }
        if (itemstack == null)
        {
            return false;
        }
        if (creativeMode)
        {
            int j1 = itemstack.getItemDamage();
            int k1 = itemstack.stackSize;
            boolean flag = itemstack.useItem(entityplayer, world, i, j, k, l);
            itemstack.setItemDamage(j1);
            itemstack.stackSize = k1;
            return flag;
        }
        else
        {
            return itemstack.useItem(entityplayer, world, i, j, k, l);
        }
    }

    public boolean sendUseItem(EntityPlayer entityplayer, World world, ItemStack itemstack)
    {
        syncCurrentPlayItem();
        netClientHandler.addToSendQueue(new Packet15Place(-1, -1, -1, 255, entityplayer.inventory.getCurrentItem()));
        boolean flag = super.sendUseItem(entityplayer, world, itemstack);
        return flag;
    }

    public EntityPlayer createPlayer(World world)
    {
        return new EntityClientPlayerMP(mc, world, mc.session, netClientHandler);
    }

    public void attackEntity(EntityPlayer entityplayer, Entity entity)
    {
        syncCurrentPlayItem();
        netClientHandler.addToSendQueue(new Packet7UseEntity(entityplayer.entityId, entity.entityId, 1));
        entityplayer.attackTargetEntityWithCurrentItem(entity);
    }

    public void interactWithEntity(EntityPlayer entityplayer, Entity entity)
    {
        syncCurrentPlayItem();
        netClientHandler.addToSendQueue(new Packet7UseEntity(entityplayer.entityId, entity.entityId, 0));
        entityplayer.useCurrentItemOnEntity(entity);
    }

    public ItemStack windowClick(int i, int j, int k, boolean flag, EntityPlayer entityplayer)
    {
        short word0 = entityplayer.craftingInventory.func_20111_a(entityplayer.inventory);
        ItemStack itemstack = super.windowClick(i, j, k, flag, entityplayer);
        netClientHandler.addToSendQueue(new Packet102WindowClick(i, j, k, flag, itemstack, word0));
        return itemstack;
    }

    public void func_40593_a(int i, int j)
    {
        netClientHandler.addToSendQueue(new Packet108EnchantItem(i, j));
    }

    public void func_35637_a(ItemStack itemstack, int i)
    {
        if (creativeMode)
        {
            netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(i, itemstack));
        }
    }

    public void func_35639_a(ItemStack itemstack)
    {
        if (creativeMode && itemstack != null)
        {
            netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(-1, itemstack));
        }
    }

    public void func_20086_a(int i, EntityPlayer entityplayer)
    {
        if (i == -9999)
        {
            return;
        }
        else
        {
            return;
        }
    }

    public void onStoppedUsingItem(EntityPlayer entityplayer)
    {
        syncCurrentPlayItem();
        netClientHandler.addToSendQueue(new Packet14BlockDig(5, 0, 0, 0, 255));
        super.onStoppedUsingItem(entityplayer);
    }

    public boolean func_35642_f()
    {
        return true;
    }

    public boolean func_35641_g()
    {
        return !creativeMode;
    }

    public boolean isInCreativeMode()
    {
        return creativeMode;
    }

    public boolean extendedReach()
    {
        return creativeMode;
    }
}
