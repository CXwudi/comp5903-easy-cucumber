package scs.comp5903.cucumber.parser.samplestepdef;

import scs.comp5903.cucumber.model.annotation.step.JStep;
import scs.comp5903.cucumber.model.annotation.step.JStepKeyword;

/**
 * @author CX无敌
 * @date 2022-08-17
 */
public class SampleStepDefinition {

  private static void notAStepDefinitionMethod() {

  }

  public void notAStepDefinition() {

  }

  @JStep(keyword = JStepKeyword.GIVEN, value = "I have {int} apples")
  public void givenIHaveApples(int count) {

  }

  @JStep(keyword = JStepKeyword.WHEN, value = "I eat {int} apples")
  public void whenIEatApples(int count) {

  }

  @JStep(keyword = JStepKeyword.THEN, value = "I should have {int} apples")
  public void thenIShouldHaveApples(int count) {

  }

}
