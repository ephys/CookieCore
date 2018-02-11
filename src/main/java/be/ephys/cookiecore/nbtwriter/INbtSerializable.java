package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.NBTTagCompound;

public interface INbtSerializable {

  NBTTagCompound writeToNBT(NBTTagCompound tag);

  void readFromNBT(NBTTagCompound tag);
}
