# CookieCore

Library mod for ephys' mods.

## Config Generation

In the constructor of your mod, add `ConfigSynchronizer.synchronizeConfig();` (`be.ephys.cookiecore.config.ConfigSynchronizer`) to launch config synchronization for your mod.

Then in any class, you can declare static configuration fields:

```java
class MyFeature {
  @Config(name = "myfeature.enabled", description = "Enable 'MyFeature'")
  @Config.BooleanDefault(value = true)
  public static ForgeConfigSpec.BooleanValue enabled;
}
```

Fields annotated with `@Config` + `@Config.<type>Default` will be set to an instance of `ForgeConfigSpec.ConfigValue` that you can use.

One of the `@Config.<type>Default` annotations *must* be specified.

### Supported config types

**string:**

```java
class MyFeature {
  @Config(name = "namespace.subnamespace.key")
  @Config.StringDefault("default value")
  public static ForgeConfigSpec.ConfigValue<String> myConfigValue;
}
```

**string list:**

*important*: Due to limitations in both the TOML parser, and Java annotations, the `StringListDefault` accepts an ARRAY of strings,
    but the `ForgeConfigSpec.ConfigValue<>` must use a `List` as its generic!

```java
class MyFeature {
  @Config(name = "namespace.subnamespace.key")
  @Config.StringListDefault({"value1", "value2"})
  public static ForgeConfigSpec.ConfigValue<List<String>> myConfigValue;
}
```

**boolean:**

```java
class MyFeature {
  @Config(name = "myfeature.enabled", description = "Enable 'MyFeature'")
  @Config.BooleanDefault(value = true)
  public static ForgeConfigSpec.BooleanValue enabled;
}
```

**int:**

```java
class MyFeature {
  @Config(name = "range_computation.base_range", description = "What is the beacon base range?")
  @Config.IntDefault(10)
  public static ForgeConfigSpec.IntValue baseRange;
}
```

**long:**

```java
class MyFeature {
  @Config(name = "range_computation.base_range", description = "What is the beacon base range?")
  @Config.LongDefault(10)
  public static ForgeConfigSpec.LongValue baseRange;
}
```

**double:**

```java
class MyFeature {
  @Config(name = "range_computation.base_range", description = "What is the beacon base range?")
  @Config.DoubleDefault(10.5)
  public static ForgeConfigSpec.DoubleValue baseRange;
}
```

*enum:**

```java
class MyFeature {
  @Config(name = "range_computation.vertical_range_type")
  @Config.EnumDefault(value = "FullHeight", enumType = BeaconVerticalRangeType.class)
  // BeaconVerticalRangeType is an enum
  public static ForgeConfigSpec.EnumValue<BeaconVerticalRangeType> verticalRangeType;
}
```

### Escape hatch

If you need to build part of the config object yourself, you can use `@OnBuildConfig` on a *static* method.

```java
class MyFeature {
  public static ForgeConfigSpec.IntValue configValue;
  
  @OnBuildConfig()
  public static onBuildConfig(ForgeConfigSpec.Builder rootBuilder) {
    configValue = rootBuilder.defineInRange("my_config_value", 5, 0, 10);
  }
}
```
