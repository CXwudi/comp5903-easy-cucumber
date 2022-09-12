package scs.comp5903.cucumber.model.annotation;

import scs.comp5903.cucumber.model.JStepKeyword;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JStep {
  JStepKeyword keyword();

  String value();
}
