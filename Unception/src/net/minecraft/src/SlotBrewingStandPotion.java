package net.minecraft.src;

class SlotBrewingStandPotion extends Slot
{
    private EntityPlayer player;
    final ContainerBrewingStand container;

    public SlotBrewingStandPotion(ContainerBrewingStand containerbrewingstand, EntityPlayer entityplayer, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        container = containerbrewingstand;
        player = entityplayer;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return itemstack != null && (itemstack.itemID == Item.potion.shiftedIndex || itemstack.itemID == Item.glassBottle.shiftedIndex);
    }

    public int getSlotStackLimit()
    {
        return 1;
    }

    public void onPickupFromSlot(ItemStack itemstack)
    {
        if (itemstack.itemID == Item.potion.shiftedIndex && itemstack.getItemDamage() > 0)
        {
            player.addStat(AchievementList.potion, 1);
        }
        super.onPickupFromSlot(itemstack);
    }
}
