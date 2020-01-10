package be.ephys.cookiecore.config;

import be.ephys.cookiecore.core.CookieCore;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

  // TODO move to instance
  private static String modId;
  private static List<ModFileScanData.AnnotationData> sidedAnnotations = null;
  private static boolean requiresExplicitModId;

  public static Pair<DynamicForgeConfigSpec, ForgeConfigSpec> synchronizeConfig() {
    ModLoadingContext modLoadingContext = ModLoadingContext.get();
    modId = modLoadingContext.getActiveContainer().getModId();

    CookieCore.getLogger().info("Syncing config fields from mod " + modId);

    // ============================================================
    // step 1: discover configurations
    // ============================================================

    Set<ModFileScanData.AnnotationData> annotations = null;
    requiresExplicitModId = false;

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

    // TODO: split by side
    // COMMON
    // CLIENT
    // SERVER

    sidedAnnotations = configTargets;

    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    final Pair<DynamicForgeConfigSpec, ForgeConfigSpec> specPair = builder.configure(DynamicForgeConfigSpec::new);
    ForgeConfigSpec spec = specPair.getRight();


    modLoadingContext.registerConfig(ModConfig.Type.CLIENT, spec);

    return specPair;
  }

  public static final class DynamicForgeConfigSpec {
    public DynamicForgeConfigSpec(final ForgeConfigSpec.Builder rootBuilder) {
      for (ModFileScanData.AnnotationData annotation : sidedAnnotations) {

        // TODO: config fields must be of type ForgeConfigSpec.ConfigValue<>

        Field field;
        try {
          Class clazz = Class.forName(annotation.getClassType().getClassName());
          field = clazz.getField(annotation.getMemberName());
        } catch (ClassNotFoundException | NoSuchFieldException e) {
          throw new RuntimeException("Failed to load config for mod " + modId, e);
        }

        if (!ForgeConfigSpec.ConfigValue.class.isAssignableFrom(field.getDeclaringClass())) {
          throw new RuntimeException("Failed to load config: @Config can only be used on fields of type ForgeConfigSpec.ConfigValue");
        }

        if (!Modifier.isStatic(field.getModifiers())) {
          throw new RuntimeException("Failed to load config: @Config can only be used on static fields");
        }

        field.setAccessible(true);

        Config configMeta = field.getAnnotation(Config.class);

        if (requiresExplicitModId) {
          if (configMeta.modId().length() == 0) {
            CookieCore.getLogger().error("@Config Failed to determine modId for fields in class " + field.getDeclaringClass().getCanonicalName());
          }

          if (!configMeta.modId().equals(ConfigSynchronizer.modId)) {
            continue;
          }
        }

        ForgeConfigSpec.Builder builder = rootBuilder
          .comment(configMeta.description());

        if (configMeta.requiresWorldRestart()) {
          builder = builder.worldRestart();
        }

        // TODO:
        //  - Check the various Config.x annotations & assign the right type
//        ForgeConfigSpec.ConfigValue configValue = builder.define(configMeta.name());

        try {
          field.set(null, builder);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
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
}
