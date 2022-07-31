package scs.comp5903.cucumber.model.jstep;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */

class AbstractJStepTest {
  @Test
  void checkEqual() {
    var andStep1 = new AndStep("I am a step");
    var andStep2 = new AndStep("I am a step");
    assertEquals(andStep1, andStep2);
  }

  @Test
  void checkNotEqual() {
    var andStep1 = new AndStep("I am a step");
    var andStep2 = new AndStep("I am a step too");
    assertNotEquals(andStep1, andStep2);
  }

  @Test
  void checkNotEqual2() {
    var andStep = new AndStep("I am a step");
    var butStep = new ButStep("I am a step");
    assertNotEquals(andStep, butStep);
  }
}