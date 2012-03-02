package net.minecraft.src;

class SlotBrewingStandIngredient extends Slot
{
    final ContainerBrewingStand container;

    public SlotBrewingStandIngredient(ContainerBrewingStand containerbrewingstand, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        container = containerbrewingstand;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        if (itemstack != null)
        {
            return Item.itemsList[itemstack.itemID].isPotionIngredient();
        }
        else
        {
            return false;
        }
    }

    public int getSlotStackLimit()
    {
        return 64;
    }
}
