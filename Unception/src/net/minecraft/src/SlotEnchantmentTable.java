package net.minecraft.src;

class SlotEnchantmentTable extends InventoryBasic
{
    final ContainerEnchantment container;

    SlotEnchantmentTable(ContainerEnchantment containerenchantment, String s, int i)
    {
        super(s, i);
        container = containerenchantment;
    }

    public int getInventoryStackLimit()
    {
        return 1;
    }

    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        container.onCraftMatrixChanged(this);
    }
}
