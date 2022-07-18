package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;

public class BooleanNbtWriter implements NbtWriter<Boolean> {

  @Override
  public Tag toNbt(Boolean data) {
    return ByteTag.valueOf(data);
  }

  @Override
  public Boolean fromNbt(Tag nbt) {
    return ((ByteTag) nbt).getAsByte() == 1;
  }
}
