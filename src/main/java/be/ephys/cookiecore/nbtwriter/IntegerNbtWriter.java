package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;

public class IntegerNbtWriter implements NbtWriter<Integer> {

  @Override
  public INBT toNbt(Integer data) {
    return IntNBT.valueOf(data);
  }

  @Override
  public Integer fromNbt(INBT nbt) {
    return ((IntNBT) nbt).getInt();
  }
}
