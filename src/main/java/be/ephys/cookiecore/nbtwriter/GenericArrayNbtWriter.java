package be.ephys.cookiecore.nbtwriter;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.lang.reflect.Array;

public class GenericArrayNbtWriter<T> implements NbtWriter<T[]> {

  private final NbtWriter<T> componentWriter;
  private final Class<T> componentClass;

  public GenericArrayNbtWriter(NbtWriter<T> writer, Class<T> componentClass) {
    this.componentWriter = writer;
    this.componentClass = componentClass;
  }

  @Override
  public Tag toNbt(T[] data) {
    ListTag tagList = new ListTag();

    // this is a hack to note the length of the array as we cannot store nulls in the array
    CompoundTag finalTag = new CompoundTag();
    finalTag.putInt("ganw__length", data.length);

    for (int i = 0; i < data.length; i++) {
      T datum = data[i];

      if (datum == null) {
        continue;
      }

      Tag componentNbt = componentWriter.toNbt(datum);
      if (!(componentNbt instanceof CompoundTag)) {
        CompoundTag wrapper = new CompoundTag();
        wrapper.put("ganw__item", componentNbt);

        componentNbt = wrapper;
      }

      CompoundTag subtag = (CompoundTag) componentNbt;
      subtag.putInt("ganw__slot", i);

      tagList.add(subtag);
    }

    return tagList;
  }

  @Override
  public T[] fromNbt(Tag nbt) {
    ListTag tagList = (ListTag) nbt;

    int length = ((ListTag) nbt).getCompound(0).getInt("ganw__length");

    final T[] array = (T[]) Array.newInstance(this.componentClass, length);

    int tagCount = ((ListTag) nbt).size();
    for (int i = 1; i < tagCount; i++) {
      CompoundTag tagMeta = tagList.getCompound(i);
      int slot = tagMeta.getInt("ganw__slot");

      Tag tag = tagMeta.contains("ganw__slot") ? tagMeta.get("ganw__item") : tagMeta;

      array[slot] = this.componentWriter.fromNbt(tag);
    }

    return array;
  }
}
