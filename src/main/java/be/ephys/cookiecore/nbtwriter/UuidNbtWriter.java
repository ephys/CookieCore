package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

import java.util.UUID;

public class UuidNbtWriter implements NbtWriter<UUID> {

  @Override
  public Tag toNbt(UUID data) {
    return NbtUtils.createUUID(data);
  }

  @Override
  public UUID fromNbt(Tag nbt) {
    return NbtUtils.loadUUID(nbt);
  }
}
