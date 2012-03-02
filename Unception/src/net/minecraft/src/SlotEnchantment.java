package net.minecraft.src;

class SlotEnchantment extends Slot
{
    final ContainerEnchantment container;

    SlotEnchantment(ContainerEnchantment containerenchantment, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        container = containerenchantment;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return true;
    }
}
