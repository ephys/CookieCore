package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;

public class IntegerNbtWriter implements NbtWriter<Integer> {

  @Override
  public NBTBase toNbt(Integer data) {
    return new NBTTagInt(data);
  }

  @Override
  public Integer fromNbt(NBTBase nbt) {
    return ((NBTTagInt) nbt).getInt();
  }
}
