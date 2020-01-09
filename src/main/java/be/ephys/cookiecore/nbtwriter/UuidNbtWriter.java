package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;

import java.util.UUID;

public class UuidNbtWriter implements NbtWriter<UUID> {

  @Override
  public INBT toNbt(UUID data) {
    return NBTUtil.writeUniqueId(data);
  }

  @Override
  public UUID fromNbt(INBT nbt) {
    return NBTUtil.readUniqueId((CompoundNBT) nbt);
  }
}
