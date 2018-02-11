package be.ephys.cookiecore.helpers;

import be.ephys.cookiecore.nbtwriter.NbtWriter;
import be.ephys.cookiecore.nbtwriter.NbtWriterRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class NBTHelper {

  public static NBTTagCompound getNBT(ItemStack stack) {
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }

    return stack.getTagCompound();
  }

  public static void genericWrite(NBTTagCompound tag, String fieldName, Object data) {

    if (data == null) {
      return;
    }

    NbtWriter writer = NbtWriterRegistry.getWriter(data.getClass());

    if (writer == null) {
      throw new RuntimeException("No NBT writer has been registered for class " + data.getClass().getCanonicalName());
    }

    writer.writeToNbt(tag, fieldName, data);
  }

  public static <T> T genericRead(NBTTagCompound tag, String fieldName, Class<T> dataClass) {
    NbtWriter writer = NbtWriterRegistry.getWriter(dataClass);

    if (writer == null) {
      throw new RuntimeException("No NBT writer has been registered for class " + dataClass.getCanonicalName());
    }

    return (T) writer.readFromNbt(tag, fieldName);
  }
}
