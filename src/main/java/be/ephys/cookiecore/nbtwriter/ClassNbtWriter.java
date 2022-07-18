package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class ClassNbtWriter implements NbtWriter<Class> {

  @Override
  public Tag toNbt(Class data) {
    return StringTag.valueOf(data.getCanonicalName());
  }

  @Override
  public Class fromNbt(Tag nbt) {
    String string = nbt.getAsString();

    try {
      return Class.forName(string);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
