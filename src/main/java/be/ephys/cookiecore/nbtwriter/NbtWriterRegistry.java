package be.ephys.cookiecore.nbtwriter;

import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class NbtWriterRegistry {

  private static final Map<Class<?>, NbtWriter> writers = new WeakHashMap<>();
  private static final Map<Class<? extends NbtWriter>, NbtWriter> writerInstances = new WeakHashMap<>();

  static {
    registerWriter(String.class, new StringNbtWriter());
    registerWriter(Integer.class, new IntegerNbtWriter());
    registerWriter(Boolean.class, new BooleanNbtWriter());

    registerWriter(ItemStack.class, new ItemStackNbtWriter());
    registerWriter(UUID.class, new UuidNbtWriter());
    registerWriter(Class.class, new ClassNbtWriter());

    registerWriter(int[].class, new IntArrayNbtWriter());

    // TODO support for fluidstack
  }

  public static <T> void registerWriter(Class<T> writable, NbtWriter<T> writer) {
    writers.put(writable, writer);
    writerInstances.put(writer.getClass(), writer);

    Class boxed = getBoxed(writable);
    if (boxed != null) {
      writers.put(boxed, writer);
    }

    Class primitive = getPrimitive(writable);
    if (primitive != null) {
      writers.put(primitive, writer);
    }
  }

  public static Class getBoxed(Class primitiveType) {
    if (!primitiveType.isPrimitive()) {
      return null;
    }

    if (primitiveType == byte.class) {
      return Byte.class;
    }

    if (primitiveType == boolean.class) {
      return Boolean.class;
    }

    if (primitiveType == char.class) {
      return Character.class;
    }

    if (primitiveType == double.class) {
      return Double.class;
    }

    if (primitiveType == float.class) {
      return Float.class;
    }

    if (primitiveType == int.class) {
      return Integer.class;
    }

    if (primitiveType == long.class) {
      return Long.class;
    }

    if (primitiveType == short.class) {
      return Short.class;
    }

    if (primitiveType == void.class) {
      return Void.class;
    }

    return null;
  }

  public static Class getPrimitive(Class boxedType) {
    if (boxedType.isPrimitive()) {
      return null;
    }

    if (boxedType == Byte.class) {
      return byte.class;
    }

    if (boxedType == Boolean.class) {
      return boolean.class;
    }

    if (boxedType == Character.class) {
      return char.class;
    }

    if (boxedType == Double.class) {
      return double.class;
    }

    if (boxedType == Float.class) {
      return float.class;
    }

    if (boxedType == Integer.class) {
      return int.class;
    }

    if (boxedType == Long.class) {
      return long.class;
    }

    if (boxedType == Short.class) {
      return short.class;
    }

    if (boxedType == Void.class) {
      return void.class;
    }

    return null;
  }

  public static <T> NbtWriter<T> getInstance(Class<? extends NbtWriter> writerClass) {

    if (writerInstances.containsKey(writerClass)) {
      return writerInstances.get(writerClass);
    }

    try {
      NbtWriter<T> instance = writerClass.newInstance();
      writerInstances.put(writerClass, instance);

      return instance;
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> NbtWriter<T> getWriter(Class<T> writable) {
    if (writers.containsKey(writable)) {
      return writers.get(writable);
    }

    if (!writable.isArray()) {
      return null;
    }

    Class<?> arrayComponentClass = writable.getComponentType();

    if (!writers.containsKey(arrayComponentClass)) {
      return null;
    }

    NbtWriter cache = new GenericArrayNbtWriter(writers.get(arrayComponentClass), arrayComponentClass);
    registerWriter(writable, cache);

    return cache;
  }
}
