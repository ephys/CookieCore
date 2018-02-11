package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

public class ClassNbtWriter implements NbtWriter<Class> {

  @Override
  public NBTBase toNbt(Class data) {
    return new NBTTagString(data.getCanonicalName());
  }

  @Override
  public Class fromNbt(NBTBase nbt) {
    String string = ((NBTTagString) nbt).getString();

    try {
      return Class.forName(string);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
