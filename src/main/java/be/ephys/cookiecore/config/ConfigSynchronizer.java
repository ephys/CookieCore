package be.ephys.cookiecore.config;

import be.ephys.cookiecore.core.CookieCore;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * TODO support for lists
 * TODO add @Config (class annotation) to define defaults (category, restart, etc)
 * TODO add @OnConfigChange (method annotation) to call when a field changes value (field name, new value, old value)
 * TODO on config change, update field value
 * TODO add Enum value description support
 */
public class ConfigSynchronizer {

  private final ModMetadata modMeta;
  private final Configuration configHandler;

  public static void synchronizeConfig(FMLPreInitializationEvent event) {
    new ConfigSynchronizer(event);
  }

  public static void synchronizeConfig(FMLPreInitializationEvent event, Configuration configHandler) {
    new ConfigSynchronizer(event, configHandler);
  }

  private ConfigSynchronizer(FMLPreInitializationEvent event) {
    this(event, buildConfigHandler(event));

    if (configHandler.hasChanged()) {
      configHandler.save();
    }
  }

  private ConfigSynchronizer(FMLPreInitializationEvent event, Configuration configHandler) {
    this.modMeta = event.getModMetadata();
    this.configHandler = configHandler;

    Set<ASMDataTable.ASMData> configAsm = event.getAsmData().getAll(Config.class.getCanonicalName());

    CookieCore.getLogger().info("Syncing config fields from mod " + modMeta.modId + " (" + modMeta.name + ")");
    for (ASMDataTable.ASMData asmData : configAsm) {
      // this fails if there is more than one mod in the same jar.
      if (!isFromMod(asmData, event.getModMetadata().modId)) {
        continue;
      }

      try {
        Class<?> annotatedClass = Class.forName(asmData.getClassName());
        String fieldName = asmData.getObjectName();

        Field field = annotatedClass.getField(fieldName);
        Config configMeta = field.getAnnotation(Config.class);

        syncConfigField(field, configMeta, configHandler);
      } catch (ClassNotFoundException | NoSuchFieldException e) {
        e.printStackTrace();
      }
    }

    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (!event.getModID().equals(modMeta.modId)) {
      return;
    }

    // TODO load config file, update fields that do not require MC to restart, call @OnConfigChange events for fields whose value changed
  }

  private static void syncConfigField(Field field, Config configMeta, Configuration configHandler) {
    try {
      field.setAccessible(true);

      Object defaultValue = field.get(null);

      String fieldName = configMeta.name();
      if (fieldName.equals("")) {
        fieldName = field.getName();
      }

      Object actualValue = get(
        configHandler,
        configMeta.category(),
        fieldName,
        configMeta.description(),
        configMeta.requiresMcRestart(),
        configMeta.requiresWorldRestart(),
        field.getType(),
        defaultValue
      );

      field.set(null, actualValue);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private static boolean isFromMod(ASMDataTable.ASMData asmData, String modId) {
    List<ModContainer> containedMods = asmData.getCandidate().getContainedMods();

    for (ModContainer mod : containedMods) {
      if (mod.getModId().equals(modId)) {
        return true;
      }
    }

    return false;
  }

  private static Property getPropertyByType(Configuration configHandler, String category, String key, Object defaultValue, Class<?> valueType) {

    // TODO generify
    if (valueType == Boolean.class || valueType == boolean.class) {
      return configHandler.get(category, key, (boolean) defaultValue);
    }

    if (valueType == Integer.class || valueType == int.class) {
      return configHandler.get(category, key, (int) defaultValue);
    }

    if (valueType == Double.class || valueType == double.class) {
      return configHandler.get(category, key, (double) defaultValue);
    }

    if (valueType == String.class) {
      return configHandler.get(category, key, (String) defaultValue);
    }

    if (isEnum(valueType)) {
      return configHandler.get(category, key, ((Enum) defaultValue).name());
    }

    throw new IllegalArgumentException("Unsupported type " + valueType.getCanonicalName());
  }

  private static Object getValue(Property property, Class<?> valueType) {

    // TODO generify
    if (valueType == Boolean.class || valueType == boolean.class) {
      return property.getBoolean();
    }

    if (valueType == Integer.class || valueType == int.class) {
      return property.getInt();
    }

    if (valueType == Double.class || valueType == double.class) {
      return property.getDouble();
    }

    if (valueType == String.class) {
      return property.getString();
    }

    if (isEnum(valueType)) {
      return getEnum(valueType, property.getString());
    }

    throw new IllegalArgumentException("Unsupported type " + valueType.getCanonicalName());
  }

  private static <T> T get(
    Configuration configHandler, String category, String optionName, String description,
    boolean requiresMcRestart, boolean requiresWorldRestart, Class<T> valueType, Object defaultValue
  ) {

    Property property = getPropertyByType(configHandler, category, optionName, defaultValue, valueType);

    if (isEnum(valueType)) {
      if (description.length() > 0) {
        description += "\n";
      }

      description += "Possible values (must match exactly): " + StringUtils.join(getEnums(valueType), " | ");
    }

    property.setComment(description);
    property.setRequiresMcRestart(requiresMcRestart);
    property.setRequiresWorldRestart(requiresWorldRestart);

    // noinspection unchecked
    return (T) getValue(property, valueType);
  }

  private static boolean isEnum(Class<?> aClass) {
    return Enum.class.isAssignableFrom(aClass);
  }

  private static EnumSet getEnums(Class aClass) {
    return EnumSet.allOf(aClass);
  }

  private static Enum getEnum(Class aClass, String enumName) {
    return Enum.valueOf(aClass, enumName);
  }

  private static Configuration buildConfigHandler(FMLPreInitializationEvent event) {
    Configuration configHandler = new Configuration(event.getSuggestedConfigurationFile());
    configHandler.load();

    return configHandler;
  }
}
