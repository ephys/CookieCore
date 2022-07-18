package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class StringNbtWriter implements NbtWriter<String> {

  @Override
  public Tag toNbt(String data) {
    return StringTag.valueOf(data);
  }

  @Override
  public String fromNbt(Tag nbt) {
    return nbt.getAsString();
  }
}
