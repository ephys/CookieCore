package be.ephys.cookiecore.config;

import net.minecraftforge.fml.config.ModConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {

  boolean requiresWorldRestart() default true;

  String modId() default "";

  String description() default "";

  String translationKey() default "";

  String name() default "";

  ModConfig.Type side() default ModConfig.Type.COMMON;

  @Target(value = ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface StringDefault {
    String value();
  }

  @Target(value = ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface BooleanDefault {
    boolean value();
  }

  @Target(value = ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface IntDefault {
    int value();

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;
  }

  @Target(value = ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface LongDefault {
    long value();

    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;
  }

  @Target(value = ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface DoubleDefault {
    double value();

    double min() default -Double.MAX_VALUE;

    double max() default Double.MAX_VALUE;
  }

  @Target(value = ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface EnumDefault {
    String value();

    Class<? extends Enum<?>> enumType();
  }
}
