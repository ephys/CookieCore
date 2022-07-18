package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;

public class IntegerNbtWriter implements NbtWriter<Integer> {

  @Override
  public Tag toNbt(Integer data) {
    return IntTag.valueOf(data);
  }

  @Override
  public Integer fromNbt(Tag nbt) {
    return ((IntTag) nbt).getAsInt();
  }
}
