package nf.fr.ephys.cookiecore.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotIInventory extends Slot {

  public SlotIInventory(IInventory inventory, int id, int posX, int posY) {
    super(inventory, id, posX, posY);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return inventory.isItemValidForSlot(getSlotIndex(), stack);
  }
}