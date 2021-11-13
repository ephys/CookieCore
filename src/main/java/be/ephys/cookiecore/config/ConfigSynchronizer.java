package be.ephys.cookiecore.config;

import be.ephys.cookiecore.core.CookieCore;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO support for lists
 * TODO support fields that are not of type ForgeConfigSpec.ConfigValue
 * - use type & value to generate data & default value
 * - directly put back the proper value
 * TODO listeners for config changes
 * TODO Add @Config.EnumDescription() to provide a description of enums
 */
public final class ConfigSynchronizer {

  private static final Type AT_CONFIG_TYPE = Type.getType(Config.class);
  private static final Type AT_ON_BUILD_CONFIG_TYPE = Type.getType(Config.OnBuildConfig.class);

  public static Map<ModConfig.Type, Pair<BuiltConfig, ForgeConfigSpec>> synchronizeConfig() {
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
    for (ModFileScanData scanData : ModList.get().getAllScanData()) {
      boolean isMultiModPackage = false;

      List<IModFileInfo> modFileInfos = scanData.getIModInfoData();

      // More than one mod file found in this zip
      if (modFileInfos.size() > 1) {
        isMultiModPackage = true;
      }

      for (IModFileInfo modFileInfo : modFileInfos) {
        List<IModInfo> mods = modFileInfo.getMods();
        // More than one mod file found in this class
        if (mods.size() > 1) {
          isMultiModPackage = true;
        }

        for (IModInfo mod : mods) {
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
      .filter(annotationData -> {
        return AT_CONFIG_TYPE.equals(annotationData.getAnnotationType())
          || AT_ON_BUILD_CONFIG_TYPE.equals(annotationData.getAnnotationType());
      })
      .collect(Collectors.toList());

    // ==============================================================
    // step 2: split by COMMON / CLIENT / SERVER config type & build
    // ==============================================================

    List<ModFileScanData.AnnotationData> commonConfigTargets = new ArrayList<>();
    List<ModFileScanData.AnnotationData> clientConfigTargets = new ArrayList<>();
    List<ModFileScanData.AnnotationData> serverConfigTargets = new ArrayList<>();

    for (ModFileScanData.AnnotationData configTarget : configTargets) {
      ModAnnotation.EnumHolder configTypeHolder = (ModAnnotation.EnumHolder) configTarget.getAnnotationData().get("side");
      ModConfig.Type configType = configTypeHolder == null
        ? ModConfig.Type.COMMON
        : ModConfig.Type.valueOf(configTypeHolder.getValue());

      if (configType == ModConfig.Type.SERVER) {
        serverConfigTargets.add(configTarget);
        continue;
      }

      if (configType == ModConfig.Type.CLIENT) {
        clientConfigTargets.add(configTarget);
        continue;
      }

      commonConfigTargets.add(configTarget);
    }

    Map<ModConfig.Type, Pair<BuiltConfig, ForgeConfigSpec>> specPairMap = new HashMap<>();

    if (commonConfigTargets.size() > 0) {
      specPairMap.put(ModConfig.Type.COMMON, buildAndRegisterConfig(modId, requiresExplicitModId, ModConfig.Type.COMMON, commonConfigTargets));
    }

    if (clientConfigTargets.size() > 0) {
      specPairMap.put(ModConfig.Type.CLIENT, buildAndRegisterConfig(modId, requiresExplicitModId, ModConfig.Type.CLIENT, clientConfigTargets));
    }

    if (serverConfigTargets.size() > 0) {
      specPairMap.put(ModConfig.Type.SERVER, buildAndRegisterConfig(modId, requiresExplicitModId, ModConfig.Type.SERVER, serverConfigTargets));
    }

    return specPairMap;
  }

  private static Pair<BuiltConfig, ForgeConfigSpec> buildAndRegisterConfig(
    String modId,
    boolean requiresExplicitModId,
    ModConfig.Type configType,
    List<ModFileScanData.AnnotationData> configFields
  ) {

    Pair<BuiltConfig, ForgeConfigSpec> configPair = BuiltConfig.build(modId, configFields, requiresExplicitModId);

    ModLoadingContext.get().registerConfig(configType, configPair.getRight());

    return configPair;
  }

  private static <E extends Enum<E>> ForgeConfigSpec.ConfigValue<?> defineConfigValue(ForgeConfigSpec.Builder builder, String name, Field field) {
    {
      Config.StringDefault annotationString = field.getAnnotation(Config.StringDefault.class);
      if (annotationString != null) {
        assertIsConfigValue(field, String.class);

        return builder.define(name, annotationString.value());
      }
    }

    {
      Config.BooleanDefault annotationBoolean = field.getAnnotation(Config.BooleanDefault.class);
      if (annotationBoolean != null) {
        assertIsConfigValue(field, boolean.class);

        return builder.define(name, annotationBoolean.value());
      }
    }

    {
      Config.IntDefault annotationInt = field.getAnnotation(Config.IntDefault.class);
      if (annotationInt != null) {
        assertIsConfigValue(field, int.class);

        // defineInRange
        return builder.defineInRange(name, annotationInt.value(), annotationInt.min(), annotationInt.max());
      }
    }

    {
      Config.LongDefault annotation = field.getAnnotation(Config.LongDefault.class);
      if (annotation != null) {
        assertIsConfigValue(field, long.class);

        // defineInRange
        return builder.defineInRange(name, annotation.value(), annotation.min(), annotation.max());
      }
    }

    {
      Config.DoubleDefault annotationDouble = field.getAnnotation(Config.DoubleDefault.class);
      if (annotationDouble != null) {
        assertIsConfigValue(field, double.class);

        return builder.defineInRange(name, annotationDouble.value(), annotationDouble.min(), annotationDouble.max());
      }
    }

    {
      Config.EnumDefault annotationEnum = field.getAnnotation(Config.EnumDefault.class);
      if (annotationEnum != null) {
        assertIsConfigValue(field, Enum.class);

        @SuppressWarnings("unchecked")
        Class<E> enumClass = (Class<E>) annotationEnum.enumType();

        E defaultValue = getEnum(enumClass, annotationEnum.value());
        Collection<E> acceptableValues = getEnums(enumClass);

        return builder.defineEnum(name, defaultValue, acceptableValues);
      }

      {
        Config.StringListDefault annotation = field.getAnnotation(Config.StringListDefault.class);

        if (annotation != null) {
          assertIsList(field, String.class);
          String[] value = annotation.value();

          return builder.defineList(name, Arrays.asList(value), val -> val.getClass() == String.class);
        }
      }
    }

    throw new RuntimeException(
      "[CookieCore Config] Failed to determine type of config field "
        + field.getDeclaringClass().getName() + "." + field.getName()
        + ". Please specify it using one of the available @Config.<type>Default annotations");
  }

  private static void assertIsConfigValue(Field field, Class<?> genericType) {
    // TODO: check generic type

    if (
      !ForgeConfigSpec.ConfigValue.class.isAssignableFrom(field.getType())
    ) {
      throw new Error("[CookieCore Config] Field "
        + field.getDeclaringClass().getName() + "." + field.getName()
        + " must be of type ForgeConfigSpec.ConfigValue<" + genericType.getName() + "> (got " + field.getType().getName() + ")"
      );
    }
  }

  private static void assertIsList(Field field, Class<?> generic) {
    assertIsConfigValue(field, List.class);

    // TODO: check list generic type
  }

  private static <T extends Enum<T>> EnumSet<T> getEnums(Class<T> aClass) {
    return EnumSet.allOf(aClass);
  }

//  private static boolean isEnum(Class<?> aClass) {
//    return Enum.class.isAssignableFrom(aClass);
//  }

  private static <T extends Enum<T>> T getEnum(Class<T> aClass, String enumName) {
    return Enum.valueOf(aClass, enumName);
  }

  public static final class BuiltConfig {
    private final List<ModFileScanData.AnnotationData> configFields;
    private final String modId;
    private final boolean requiresExplicitModId;

    private final Map<String, ForgeConfigSpec.ConfigValue<?>> configValues = new HashMap<>();

    public BuiltConfig(String modId, List<ModFileScanData.AnnotationData> configFields, boolean requiresExplicitModId) {
      this.configFields = configFields;
      this.modId = modId;
      this.requiresExplicitModId = requiresExplicitModId;
    }

    public static Pair<BuiltConfig, ForgeConfigSpec> build(String modId, List<ModFileScanData.AnnotationData> configFields, boolean requiresExplicitModId) {
      BuiltConfig specBuilder = new BuiltConfig(modId, configFields, requiresExplicitModId);

      ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
      final Pair<BuiltConfig, ForgeConfigSpec> specPair = builder.configure(specBuilder::build);

      return specPair;
    }

    public ForgeConfigSpec.ConfigValue<?> getConfigValue(String configKey) {
      return configValues.get(configKey);
    }

    private static String getMethodNameFromSignature(String signature) {
      int index = signature.indexOf('(');
      if (index == -1) {
        return signature;
      }

      return signature.substring(0, index);
    }

    private void callOnBuildConfigHook(final ModFileScanData.AnnotationData annotation, final ForgeConfigSpec.Builder rootBuilder) {
      Method method;
      try {
        Class<?> clazz = Class.forName(annotation.getClassType().getClassName());
        String methodName = getMethodNameFromSignature(annotation.getMemberName());
        method = clazz.getMethod(methodName, ForgeConfigSpec.Builder.class);
      } catch(NoSuchMethodException e) {
        throw new RuntimeException("Failed to call OnBuildConfig hook for mod " + modId
          + ". Is " + annotation.getClassType().getClassName() + "." + annotation.getMemberName()
          + " a static method and does it accept a single Parameter of type ForgeConfigSpec.Builder?", e);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Failed to load config for mod " + modId, e);
      }

      if (!Modifier.isStatic(method.getModifiers())) {
        throw new RuntimeException("Failed to load config: @Config.OnBuildConfig can only be used on static methods");
      }

      try {
        method.invoke(null, rootBuilder);
      } catch (IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException("Failed to call OnBuildConfig hook for mod " + modId
          + ". Is " + annotation.getClassType().getClassName() + "." + annotation.getMemberName()
          + " public & static ?", e);
      }
    }

    private BuiltConfig build(final ForgeConfigSpec.Builder rootBuilder) {
      for (ModFileScanData.AnnotationData annotation : configFields) {

        if (annotation.getAnnotationType().equals(AT_ON_BUILD_CONFIG_TYPE)) {
          this.callOnBuildConfigHook(annotation, rootBuilder);
          continue;
        }

        Field field;
        try {
          Class<?> clazz = Class.forName(annotation.getClassType().getClassName());
          field = clazz.getField(annotation.getMemberName());
        } catch (ClassNotFoundException | NoSuchFieldException e) {
          throw new RuntimeException("Failed to load config for mod " + modId, e);
        }

        // java.lang.RuntimeException: Failed to load config: @Config can only be used on fields of type ForgeConfigSpec.ConfigValue.
        // Got net.minecraftforge.common.ForgeConfigSpec$BooleanValue
        // (field be.ephys.cookiecore.core.CookieCore enableTerracottaWorldPreset)
        if (!ForgeConfigSpec.ConfigValue.class.isAssignableFrom(field.getType())) {
          throw new RuntimeException("Failed to load config: @Config can only be used on fields of type ForgeConfigSpec.ConfigValue.\n-> Got " + field.getType().getName() + ".\n-> Field: " + field.getDeclaringClass().getName() + " " + field.getName());
        }

        if (!Modifier.isStatic(field.getModifiers())) {
          throw new RuntimeException("Failed to load config: @Config can only be used on static fields");
        }

        field.setAccessible(true);

        Config configMeta = field.getAnnotation(Config.class);

        if (this.requiresExplicitModId) {
          if (configMeta.modId().length() == 0) {
            throw new RuntimeException("@Config Failed to determine modId for fields in class " + field.getDeclaringClass().getCanonicalName());
          }

          if (!configMeta.modId().equals(this.modId)) {
            CookieCore.getLogger().error("skipping field");
            continue;
          }
        }

        ForgeConfigSpec.Builder builder = rootBuilder
          .comment(configMeta.description());

        if (!configMeta.translationKey().isEmpty()) {
          builder = builder.translation(configMeta.translationKey());
        }

        if (configMeta.requiresWorldRestart()) {
          builder = builder.worldRestart();
        }

        String configKey = configMeta.name();
        if (configKey.isEmpty()) {
          configKey = field.getName();
        }

        ForgeConfigSpec.ConfigValue<?> configValue = defineConfigValue(builder, configKey, field);

        configValues.put(configKey, configValue);

        try {
          field.set(null, configValue);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }

      return this;
    }
  }
}
