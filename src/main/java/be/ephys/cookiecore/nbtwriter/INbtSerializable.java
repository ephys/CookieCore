package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.CompoundTag;

public interface INbtSerializable {

  CompoundTag writeToNBT(CompoundTag tag);

  void readFromNBT(CompoundTag tag);
}
