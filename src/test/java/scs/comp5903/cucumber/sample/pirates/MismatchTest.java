package scs.comp5903.cucumber.sample.pirates;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.util.ResourceUtil;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author CX无敌
 * @date 2022-10-26
 */
class MismatchTest {

  @Test
  void check() {
    assertDoesNotThrow(() -> EasyCucumber.build(ResourceUtil.getResourcePath("sample/jfeature/pirates/mismatch-bug.jfeature"), MismatchBugStepDef.class).executeAll());
  }
}
