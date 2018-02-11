package be.ephys.cookiecore.syncable;

import be.ephys.cookiecore.helpers.NBTHelper;
import be.ephys.cookiecore.nbtwriter.NbtWriter;
import be.ephys.cookiecore.nbtwriter.NbtWriterRegistry;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Core class for automatic NBT persisting and synchronization.
 *
 * Use @Persist on a field to automatically save it to disk.
 * Use @Sync on a field to make it automatically sync between server and client.
 *
 * Your tile entity or or entity will need to get an instance of their persister using
 * PersisterRegistry.getPersisterFor(this);
 * and call the relevant writeToNbt/readFromNbt/sendPacket/readPacket.
 * A base implementation is available {@link PersistedTileEntity}
 */
public final class PersisterRegistry {

  private static final Map<Class<?>, Persister> persistableInstanceMapping = new WeakHashMap<>();

  public static Persister getPersisterFor(Object persistableInstance) {
    return getPersisterFor(persistableInstance.getClass());
  }

  public static Persister getPersisterFor(Class<?> persistableInstance) {
    if (persistableInstanceMapping.containsKey(persistableInstance)) {
      return persistableInstanceMapping.get(persistableInstance);
    }

    Persister persister = new Persister(persistableInstance);
    persistableInstanceMapping.put(persistableInstance, persister);

    return persister;
  }

  public static class Persister {

    private final List<Field> persistableFields = new ArrayList<>();

    private final List<Field> syncableFields = new ArrayList<>();
    private final Map<String, Field> namedFieldMap = new HashMap<>();

    public Persister(Class<?> persistableClass) {
      Field[] fields = persistableClass.getDeclaredFields();

      for (Field field : fields) {
        Sync sync = field.getAnnotation(Sync.class);
        Persist persist = field.getAnnotation(Persist.class);

        if (persist == null && sync == null) {
          continue;
        }

        if (Modifier.isStatic(field.getModifiers())) {
          throw new RuntimeException("static fields cannot be persisted nor synced. Class: " + persistableClass.getCanonicalName());
        }

        String name = getFieldName(field);
        if (namedFieldMap.containsKey(name)) {
          throw new RuntimeException("two @Sync/@Persist fields have the same name (" + name + ") in class " + persistableClass.getCanonicalName());
        }

        field.setAccessible(true);

        namedFieldMap.put(name, field);

        if (sync != null) {
          syncableFields.add(field);
        }

        if (persist != null) {
          persistableFields.add(field);
        }
      }
    }

    /**
     * Write the fields to NBT for persistence.
     * Only fields annotated with @Persist will be written.
     *
     * @param persistableInstance The tile to persist
     * @param tag                 The tag to write to.
     * @return the tag to which the data was written.
     */
    public NBTTagCompound writeToNbt(Object persistableInstance, NBTTagCompound tag) {

      for (Field persistableField : persistableFields) {
        writeFieldToNbt(persistableField, persistableInstance, tag, ActionType.PERSIST);
      }

      return tag;
    }

    /**
     * Populates the tile entity with data read from disk.
     * Only fields annotated with @Persist will be read.
     *
     * @param persistableInstance The tile to persist.
     * @param tag                 The tag to write to.
     */
    public void readFromNbt(Object persistableInstance, NBTTagCompound tag) {

      for (Field persistableField : persistableFields) {
        readFieldFromNbt(persistableField, persistableInstance, tag, ActionType.PERSIST);
      }
    }

    /**
     * Writes the initial data to send to the client when it loads a chunk.
     * Only fields annotated with @Sync will be written.
     *
     * @param persistableInstance The tile to send to the client.
     * @param tag                 The tag to write to.
     * @return the tag to which the data was written.
     */
    public NBTTagCompound writeToUpdateTag(Object persistableInstance, NBTTagCompound tag) {

      for (Field syncableField : syncableFields) {
        writeFieldToNbt(syncableField, persistableInstance, tag, ActionType.SYNC);
      }

      return tag;
    }

    /**
     * Reads and populates the TE fields with the initial data to send from the server when the client loads a chunk.
     * Only fields annotated with @Sync will be read.
     *
     * @param persistableInstance The tile to send to the client.
     * @param tag                 The tag to write to.
     */
    public void readFromUpdateTag(Object persistableInstance, NBTTagCompound tag) {

      for (Field syncableField : syncableFields) {
        readFieldFromNbt(syncableField, persistableInstance, tag, ActionType.SYNC);
      }
    }

    /**
     * Creates a packet containing syncable fields that have changed since the last synchronisation.
     *
     * @param persistableInstance The tile to send to the client.
     */
    public NBTTagCompound writeDesynchronizedFieldsToUpdateTag(Object persistableInstance, NBTTagCompound tag) {

      // TODO only send those that changed

      return writeToUpdateTag(persistableInstance, tag);
    }

    // =======
    //  Utils
    // =======

    private Object getFieldValue(Field field, Object persistableInstance) {
      try {
        return field.get(persistableInstance);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    private void setFieldValue(Field field, Object persistableInstance, Object value) {
      try {
        field.set(persistableInstance, value);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    private String getFieldName(Field field) {
      Persist persistMeta = field.getAnnotation(Persist.class);
      String persistName = persistMeta != null ? persistMeta.name() : "";

      Sync syncMeta = field.getAnnotation(Sync.class);
      String syncName = syncMeta != null ? syncMeta.name() : "";

      if (!persistName.equals("") && !syncName.equals("")) {
        throw new RuntimeException("Cannot set field name in both @Sync and @Persist (from: " + field.getDeclaringClass().getCanonicalName() + ")");
      }

      if (!persistName.equals("")) {
        return persistName;
      }

      if (!syncName.equals("")) {
        return syncName;
      }

      return field.getName();
    }

    private NbtWriter getSerializer(Field field, ActionType actionType) {
      NbtWriter serializer;

      if (actionType == ActionType.PERSIST) {
        serializer = getPersistNbtWriter(field);
        if (serializer != null) {
          return serializer;
        }

        serializer = getSyncNbtWriter(field);
        if (serializer != null) {
          return serializer;
        }
      } else {
        serializer = getSyncNbtWriter(field);
        if (serializer != null) {
          return serializer;
        }

        serializer = getPersistNbtWriter(field);
        if (serializer != null) {
          return serializer;
        }
      }

      return null;
    }

    private NbtWriter getPersistNbtWriter(Field field) {
      Persist persistMeta = field.getAnnotation(Persist.class);
      if (persistMeta == null || persistMeta.serializer() == NbtWriter.class) {
        return null;
      }

      return NbtWriterRegistry.getInstance(persistMeta.serializer());
    }

    private NbtWriter getSyncNbtWriter(Field field) {
      Sync syncMeta = field.getAnnotation(Sync.class);
      if (syncMeta == null || syncMeta.serializer() == NbtWriter.class) {
        return null;
      }

      return NbtWriterRegistry.getInstance(syncMeta.serializer());
    }

    private void writeFieldToNbt(Field field, Object persistableInstance, NBTTagCompound tag, ActionType actionType) {
      String fieldName = getFieldName(field);

      if (tag.hasKey(fieldName)) {
        throw new RuntimeException("Field name " + fieldName + " is already present in NBT");
      }

      Object val = getFieldValue(field, persistableInstance);

      NbtWriter serializer = getSerializer(field, actionType);
      if (serializer == null) {
        NBTHelper.genericWrite(tag, fieldName, val);
      } else {
        serializer.writeToNbt(tag, fieldName, val);
      }
    }

    private void readFieldFromNbt(Field field, Object persistableInstance, NBTTagCompound tag, ActionType actionType) {
      String name = getFieldName(field);

      if (!tag.hasKey(name)) {
        return;
      }

      NbtWriter serializer = getSerializer(field, actionType);

      Object value;
      if (serializer == null) {
        value = NBTHelper.genericRead(tag, name, field.getType());
      } else {
        value = serializer.readFromNbt(tag, name);
      }

      setFieldValue(field, persistableInstance, value);
    }

    private enum ActionType {
      SYNC, PERSIST
    }
  }
}
