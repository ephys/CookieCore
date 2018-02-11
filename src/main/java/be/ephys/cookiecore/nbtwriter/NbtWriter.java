package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public interface NbtWriter<T> {

  NBTBase toNbt(T data);

  T fromNbt(NBTBase nbt);

  default void writeToNbt(NBTTagCompound tag, String fieldName, T data) {
    tag.setTag(fieldName, this.toNbt(data));
  }

  default T readFromNbt(NBTTagCompound tag, String fieldName) {
    return fromNbt(tag.getTag(fieldName));
  }
}
