package scs.comp5903.cucumber.parser.samplestepdef;

import scs.comp5903.cucumber.model.JStepKeyword;
import scs.comp5903.cucumber.model.annotation.JStep;

/**
 * @author CX无敌
 * @date 2022-08-17
 */
public class AnotherSampleStepDefinition {

  @JStep(keyword = JStepKeyword.GIVEN, value = "I have {int} oranges")
  public void givenIHaveOranges(int count) {
  }

  @JStep(keyword = JStepKeyword.WHEN, value = "I eat {int} oranges")
  public void whenIEatOranges(int count) {
  }

  @JStep(keyword = JStepKeyword.THEN, value = "I should have {int} oranges")
  public void thenIShouldHaveOranges(int count) {
  }
}
