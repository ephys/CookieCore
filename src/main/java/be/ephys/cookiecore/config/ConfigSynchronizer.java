package be.ephys.cookiecore.config;

import be.ephys.cookiecore.core.CookieCore;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO support for lists
 * TODO add @Config (class annotation) to define defaults (category, restart, etc)
 * TODO add @OnConfigChange (method annotation) to call when a field changes value (field name, new value, old value)
 * TODO on config change, update field value
 * TODO add Enum value description support
 * TODO set validValues for Enums
 * TODO add minValue, maxValue to Config (numbers)
 */
public final class ConfigSynchronizer {

  private static final Type CONFIG_TYPE = Type.getType(Config.class);

  public static Pair<DynamicForgeConfigSpec, ForgeConfigSpec> synchronizeConfig() {
    ModLoadingContext modLoadingContext = ModLoadingContext.get();
    String modId = modLoadingContext.getActiveContainer().getModId();

    CookieCore.getLogger().info("Syncing config fields from mod " + modId);

    // ============================================================
    // step 1: discover configurations
    // ============================================================

    Set<ModFileScanData.AnnotationData> annotations = null;
    boolean requiresExplicitModId = false;

    // extract AnnotationData for @Config for a given mod
    // get all Zip files
    for (ModFileScanData scanData: ModList.get().getAllScanData()) {
      boolean isMultiModPackage = false;
      scanData.getAnnotations();

      List<IModFileInfo> modFileInfos = scanData.getIModInfoData();

      // More than one mod file found in this zip
      if (modFileInfos.size() > 1) {
        isMultiModPackage = true;
      }

      for (IModFileInfo modFileInfo: modFileInfos) {
        List<IModInfo> mods = modFileInfo.getMods();
        // More than one mod file found in this class
        if (mods.size() > 1) {
          isMultiModPackage = true;
        }

        for (IModInfo mod: mods) {
          if (mod.getModId().equals(modId)) {
            annotations = scanData.getAnnotations();
            break;
          }
        }
      }

      requiresExplicitModId = isMultiModPackage;
    }

    if (annotations == null) {
      CookieCore.getLogger().error("Could not find annotations from mod " + modId);
      annotations = new HashSet<>();
    }

    List<ModFileScanData.AnnotationData> configTargets = annotations
      .stream()
      .filter(annotationData -> CONFIG_TYPE.equals(annotationData.getAnnotationType()))
      .collect(Collectors.toList());

    // ==============================================================
    // step 2: split by COMMON / CLIENT / SERVER config type & build
    // ==============================================================

    configTargets.get(0).getAnnotationData()

    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    final Pair<DynamicForgeConfigSpec, ForgeConfigSpec> specPair = builder.configure(DynamicForgeConfigSpec::new);
    ForgeConfigSpec spec = specPair.getRight();

    // COMMON
    // CLIENT
    // SERVER
    modLoadingContext.registerConfig(ModConfig.Type.CLIENT, spec);

    return specPair;

//  ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, de.madone.nimox.config.ModConfig.spec);
//  new ConfigSynchronizer(event);
  }

  public static final class DynamicForgeConfigSpec {
    public DynamicForgeConfigSpec(ForgeConfigSpec.Builder builder) {

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

      String category = configMeta.category();
      if (category.equals("")) {
        category = "general";
      }

      Object actualValue = get(
        configHandler,
        category,
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

      EnumSet validEnums = getEnums(valueType);

      description += "Possible values (must match exactly): " + StringUtils.join(validEnums, " | ");

      String[] validValues = new String[validEnums.size()];

      int i = 0;
      for (Object validEnum : validEnums) {
        validValues[i++] = validEnum.toString();
      }

      property.setValidValues(validValues);
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
