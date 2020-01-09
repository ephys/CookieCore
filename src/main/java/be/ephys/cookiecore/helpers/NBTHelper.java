package be.ephys.cookiecore.helpers;

import be.ephys.cookiecore.nbtwriter.NbtWriter;
import be.ephys.cookiecore.nbtwriter.NbtWriterRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public final class NBTHelper {

  public static CompoundNBT getNBT(ItemStack stack) {
    if (!stack.hasTag()) {
      stack.setTag(new CompoundNBT());
    }

    return stack.getTag();
  }

  public static void genericWrite(CompoundNBT tag, String fieldName, Object data) {

    if (data == null) {
      return;
    }

    NbtWriter writer = NbtWriterRegistry.getWriter(data.getClass());

    if (writer == null) {
      throw new RuntimeException("No NBT writer has been registered for class " + data.getClass().getCanonicalName());
    }

    writer.writeToNbt(tag, fieldName, data);
  }

  public static <T> T genericRead(CompoundNBT tag, String fieldName, Class<T> dataClass) {
    NbtWriter writer = NbtWriterRegistry.getWriter(dataClass);

    if (writer == null) {
      throw new RuntimeException("No NBT writer has been registered for class " + dataClass.getCanonicalName());
    }

    return (T) writer.readFromNbt(tag, fieldName);
  }
}
