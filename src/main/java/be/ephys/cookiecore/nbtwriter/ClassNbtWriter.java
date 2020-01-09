package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;

public class ClassNbtWriter implements NbtWriter<Class> {

  @Override
  public INBT toNbt(Class data) {
    return StringNBT.func_229705_a_(data.getCanonicalName());
  }

  @Override
  public Class fromNbt(INBT nbt) {
    String string = nbt.getString();

    try {
      return Class.forName(string);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
