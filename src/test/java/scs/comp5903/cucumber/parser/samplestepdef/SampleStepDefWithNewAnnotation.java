package scs.comp5903.cucumber.parser.samplestepdef;

import scs.comp5903.cucumber.model.annotation.JGivenStep;
import scs.comp5903.cucumber.model.annotation.JThenStep;
import scs.comp5903.cucumber.model.annotation.JWhenStep;

/**
 * @author CX无敌
 * @date 2022-09-13
 */
public class SampleStepDefWithNewAnnotation {
  @JGivenStep("I have {int} apples")
  public void givenIHaveApples(int count) {

  }

  @JWhenStep("I eat {int} apples")
  public void whenIEatApples(int count) {

  }

  @JThenStep("I should have {int} apples")
  public void thenIShouldHaveApples(int count) {

  }
}
