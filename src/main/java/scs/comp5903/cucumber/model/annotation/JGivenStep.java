package scs.comp5903.cucumber.model.annotation;

import java.lang.annotation.*;

/**
 * @author CX无敌
 * @date 2022-09-12
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JGivenStep {
  String value();
}

