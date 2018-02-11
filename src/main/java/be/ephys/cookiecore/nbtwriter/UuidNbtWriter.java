package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;

import java.util.UUID;

public class UuidNbtWriter implements NbtWriter<UUID> {

  @Override
  public NBTBase toNbt(UUID data) {
    return NBTUtil.createUUIDTag(data);
  }

  @Override
  public UUID fromNbt(NBTBase nbt) {
    return NBTUtil.getUUIDFromTag((NBTTagCompound) nbt);
  }
}
