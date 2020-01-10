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

  String name() default "";

  ModConfig.Type type = ModConfig.Type.COMMON;

  @interface StringType {
    String value();
  }

  @interface BooleanType {
    boolean value();
  }

  @interface IntType {
    int value();
  }

  @interface DoubleType {
    double value();
  }

  @interface EnumType {
    String value();

    Class<? extends Enum> enumType();
  }
}
