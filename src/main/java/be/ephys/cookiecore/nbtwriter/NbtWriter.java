package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public interface NbtWriter<T> {

  Tag toNbt(T data);

  T fromNbt(Tag nbt);

  default void writeToNbt(CompoundTag tag, String fieldName, T data) {
    tag.put(fieldName, this.toNbt(data));
  }

  default T readFromNbt(CompoundTag tag, String fieldName) {
    return fromNbt(tag.get(fieldName));
  }
}
