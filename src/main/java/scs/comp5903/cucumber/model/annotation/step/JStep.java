package scs.comp5903.cucumber.model.annotation.step;

import java.lang.annotation.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JStep {
  JStepKeyword keyword();

  String value();
}
