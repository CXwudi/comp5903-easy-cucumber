package scs.comp5903.cucumber.model.annotation.hook;

import java.lang.annotation.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-10-27
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BeforeAllJScenarios {
  /**
   * The order of the hook if multiple same hook instances are defined.
   * By default, it is -1, which means the order is not specified.
   */
  int order() default -1;
}
