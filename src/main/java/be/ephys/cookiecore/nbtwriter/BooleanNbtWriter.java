package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;

public class BooleanNbtWriter implements NbtWriter<Boolean> {

  @Override
  public INBT toNbt(Boolean data) {
    return ByteNBT.valueOf(data);
  }

  @Override
  public Boolean fromNbt(INBT nbt) {
    return ((ByteNBT) nbt).getByte() == 1;
  }
}
