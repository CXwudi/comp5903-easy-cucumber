package scs.comp5903.cucumber.original;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated preserve it for reference
 */
@Deprecated(since = "0.0.1")
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JStep {
  String given() default "";
  String when() default "";
  String then() default "";
}