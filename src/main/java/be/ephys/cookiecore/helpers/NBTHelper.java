package be.ephys.cookiecore.helpers;

import be.ephys.cookiecore.nbtwriter.NbtWriter;
import be.ephys.cookiecore.nbtwriter.NbtWriterRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public final class NBTHelper {

  public static CompoundTag getNBT(ItemStack stack) {
    if (!stack.hasTag()) {
      stack.setTag(new CompoundTag());
    }

    return stack.getTag();
  }

  public static void genericWrite(CompoundTag tag, String fieldName, Object data) {

    if (data == null) {
      return;
    }

    NbtWriter writer = NbtWriterRegistry.getWriter(data.getClass());

    if (writer == null) {
      throw new RuntimeException("No NBT writer has been registered for class " + data.getClass().getCanonicalName());
    }

    writer.writeToNbt(tag, fieldName, data);
  }

  public static <T> T genericRead(CompoundTag tag, String fieldName, Class<T> dataClass) {
    NbtWriter writer = NbtWriterRegistry.getWriter(dataClass);

    if (writer == null) {
      throw new RuntimeException("No NBT writer has been registered for class " + dataClass.getCanonicalName());
    }

    return (T) writer.readFromNbt(tag, fieldName);
  }
}
