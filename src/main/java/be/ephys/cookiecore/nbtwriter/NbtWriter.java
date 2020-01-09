package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public interface NbtWriter<T> {

  INBT toNbt(T data);

  T fromNbt(INBT nbt);

  default void writeToNbt(CompoundNBT tag, String fieldName, T data) {
    tag.put(fieldName, this.toNbt(data));
  }

  default T readFromNbt(CompoundNBT tag, String fieldName) {
    return fromNbt(tag.get(fieldName));
  }
}
