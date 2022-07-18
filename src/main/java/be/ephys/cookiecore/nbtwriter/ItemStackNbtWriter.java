package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class ItemStackNbtWriter implements NbtWriter<ItemStack> {

  @Override
  public Tag toNbt(ItemStack data) {
    return data.serializeNBT();
  }

  @Override
  public ItemStack fromNbt(Tag nbt) {
    return ItemStack.of((CompoundTag) nbt);
  }
}
