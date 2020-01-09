package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.ListNBT;

import java.lang.reflect.Array;

public class GenericArrayNbtWriter<T> implements NbtWriter<T[]> {

  private final NbtWriter<T> componentWriter;
  private final Class<T> componentClass;

  public GenericArrayNbtWriter(NbtWriter<T> writer, Class<T> componentClass) {
    this.componentWriter = writer;
    this.componentClass = componentClass;
  }

  @Override
  public INBT toNbt(T[] data) {
    ListNBT tagList = new ListNBT();

    // this is a hack to note the length of the array as we cannot store nulls in the array
    CompoundNBT finalTag = new CompoundNBT();
    finalTag.putInt("ganw__length", data.length);

    for (int i = 0; i < data.length; i++) {
      T datum = data[i];

      if (datum == null) {
        continue;
      }

      INBT componentNbt = componentWriter.toNbt(datum);
      if (!(componentNbt instanceof CompoundNBT)) {
        CompoundNBT wrapper = new CompoundNBT();
        wrapper.put("ganw__item", componentNbt);

        componentNbt = wrapper;
      }

      CompoundNBT subtag = (CompoundNBT) componentNbt;
      subtag.putInt("ganw__slot", i);

      tagList.add(subtag);
    }

    return tagList;
  }

  @Override
  public T[] fromNbt(INBT nbt) {
    ListNBT tagList = (ListNBT) nbt;

    int length = ((ListNBT) nbt).getCompound(0).getInt("ganw__length");

    final T[] array = (T[]) Array.newInstance(this.componentClass, length);

    int tagCount = ((ListNBT) nbt).size();
    for (int i = 1; i < tagCount; i++) {
      CompoundNBT tagMeta = tagList.getCompound(i);
      int slot = tagMeta.getInt("ganw__slot");

      INBT tag = tagMeta.contains("ganw__slot") ? tagMeta.get("ganw__item") : tagMeta;

      array[slot] = this.componentWriter.fromNbt(tag);
    }

    return array;
  }
}
