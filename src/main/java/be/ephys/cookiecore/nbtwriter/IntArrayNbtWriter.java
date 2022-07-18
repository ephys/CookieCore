package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.Tag;

public class IntArrayNbtWriter implements NbtWriter<int[]> {

  @Override
  public Tag toNbt(int[] data) {
    return new IntArrayTag(data);
  }

  @Override
  public int[] fromNbt(Tag nbt) {
    return ((IntArrayTag) nbt).getAsIntArray();
  }
}
