package be.ephys.cookiecore.nbtwriter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;

public class ItemStackNbtWriter implements NbtWriter<ItemStack> {

  @Override
  public INBT toNbt(ItemStack data) {
    return data.serializeNBT();
  }

  @Override
  public ItemStack fromNbt(INBT nbt) {
    return ItemStack.read((CompoundNBT) nbt);
  }
}
