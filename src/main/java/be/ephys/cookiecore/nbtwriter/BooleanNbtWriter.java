package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

public class BooleanNbtWriter implements NbtWriter<Boolean> {

  @Override
  public NBTBase toNbt(Boolean data) {
    return new NBTTagByte((byte) (data ? 1 : 0));
  }

  @Override
  public Boolean fromNbt(NBTBase nbt) {
    return ((NBTTagByte) nbt).getByte() == 1;
  }
}
