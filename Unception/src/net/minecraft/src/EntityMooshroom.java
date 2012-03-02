package net.minecraft.src;

public class EntityMooshroom extends EntityCow
{
    public EntityMooshroom(World world)
    {
        super(world);
        texture = "/mob/redcow.png";
        setSize(0.9F, 1.3F);
    }

    public boolean interact(EntityPlayer entityplayer)
    {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        if (itemstack != null && itemstack.itemID == Item.bowlEmpty.shiftedIndex && getDelay() >= 0)
        {
            entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(Item.bowlSoup));
            return true;
        }
        if (itemstack != null && itemstack.itemID == Item.shears.shiftedIndex && getDelay() >= 0)
        {
            setEntityDead();
            EntityCow entitycow = new EntityCow(worldObj);
            entitycow.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            entitycow.setEntityHealth(getEntityHealth());
            entitycow.renderYawOffset = renderYawOffset;
            worldObj.spawnEntityInWorld(entitycow);
            worldObj.spawnParticle("largeexplode", posX, posY + (double)(height / 2.0F), posZ, 0.0D, 0.0D, 0.0D);
            for (int i = 0; i < 5; i++)
            {
                worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY + (double)height, posZ, new ItemStack(Block.mushroomRed)));
            }

            return true;
        }
        else
        {
            return super.interact(entityplayer);
        }
    }

    protected EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal)
    {
        return new EntityMooshroom(worldObj);
    }
}
