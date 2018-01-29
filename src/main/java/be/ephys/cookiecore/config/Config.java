package be.ephys.cookiecore.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {

  boolean requiresMcRestart() default true;

  boolean requiresWorldRestart() default true;

  String description() default "";

  String name() default "";

  String category() default "";
}
