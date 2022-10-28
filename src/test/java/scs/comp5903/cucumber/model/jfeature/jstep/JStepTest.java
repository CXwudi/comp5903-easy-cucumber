package scs.comp5903.cucumber.model.jfeature.jstep;

import scs.comp5903.cucumber.model.annotation.step.JGivenStep;
import scs.comp5903.cucumber.model.annotation.step.JStep;

import static scs.comp5903.cucumber.model.annotation.step.JStepKeyword.GIVEN;
import static scs.comp5903.cucumber.model.annotation.step.JStepKeyword.WHEN;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
class JStepTest {

  @JStep(keyword = GIVEN, value = "I am a step")
  void aFunction() {
    // do nothing, this test simply shows how to use @JStep
  }

  @JStep(keyword = WHEN, value = "I do something")
  void aFunction2() {
    // do nothing, this test simply shows how to use @JStep
  }

  @JGivenStep("I am a step")
  void aFunction3() {
    // do nothing, this test simply shows how to use @JStep
  }

}