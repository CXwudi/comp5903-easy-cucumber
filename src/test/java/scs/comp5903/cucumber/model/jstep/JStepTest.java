package scs.comp5903.cucumber.model.jstep;

import scs.comp5903.cucumber.model.JStep;

import static scs.comp5903.cucumber.model.JStepKeyword.GIVEN;
import static scs.comp5903.cucumber.model.JStepKeyword.WHEN;

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

}