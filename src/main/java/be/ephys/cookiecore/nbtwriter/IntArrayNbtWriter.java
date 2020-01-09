package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.IntArrayNBT;

public class IntArrayNbtWriter implements NbtWriter<int[]> {

  @Override
  public INBT toNbt(int[] data) {
    return new IntArrayNBT(data);
  }

  @Override
  public int[] fromNbt(INBT nbt) {
    return ((IntArrayNBT) nbt).getIntArray();
  }
}
