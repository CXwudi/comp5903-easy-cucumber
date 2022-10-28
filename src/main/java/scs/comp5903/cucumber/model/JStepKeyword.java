package scs.comp5903.cucumber.model;

import scs.comp5903.cucumber.model.annotation.step.JStep;

/**
 * the keyword used by {@link JStep} and possibly other classes
 *
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
public enum JStepKeyword {
  GIVEN("Given"), WHEN("When"), THEN("Then"), AND("And"), BUT("But");

  private final String keyword;

  JStepKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getKeyword() {
    return keyword;
  }
}
