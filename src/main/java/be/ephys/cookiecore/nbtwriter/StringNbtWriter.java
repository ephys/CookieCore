package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

public class StringNbtWriter implements NbtWriter<String> {

  @Override
  public NBTBase toNbt(String data) {
    return new NBTTagString(data);
  }

  @Override
  public String fromNbt(NBTBase nbt) {
    return ((NBTTagString) nbt).getString();
  }
}
