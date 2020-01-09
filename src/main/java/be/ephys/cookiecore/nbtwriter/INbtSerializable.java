package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.CompoundNBT;

public interface INbtSerializable {

  CompoundNBT writeToNBT(CompoundNBT tag);

  void readFromNBT(CompoundNBT tag);
}
