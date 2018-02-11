package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;

public class IntArrayNbtWriter implements NbtWriter<int[]> {

  @Override
  public NBTBase toNbt(int[] data) {
    return new NBTTagIntArray(data);
  }

  @Override
  public int[] fromNbt(NBTBase nbt) {
    return ((NBTTagIntArray) nbt).getIntArray();
  }
}
