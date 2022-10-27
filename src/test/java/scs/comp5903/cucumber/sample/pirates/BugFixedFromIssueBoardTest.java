package scs.comp5903.cucumber.sample.pirates;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.util.ResourceUtil;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class is used to verify the bug fixed from the issue board from GitHub repo
 * @author CX无敌
 * @date 2022-10-26
 */
class BugFixedFromIssueBoardTest {

  @Test
  void check1() {
    assertDoesNotThrow(() -> EasyCucumber.build(ResourceUtil.getResourcePath("sample/jfeature/pirates/mismatch-bug.jfeature"), MismatchBugStepDef.class).executeAll());
  }

  @Test
  void checkIssue12() {
    assertDoesNotThrow(() -> EasyCucumber.build(ResourceUtil.getResourcePath("sample/jfeature/pirates/similar-step.jfeature"), SimilarStepBugStepDef.class).executeAll());
    var easyCucumberException = assertThrows(EasyCucumberException.class, () -> EasyCucumber.build(ResourceUtil.getResourcePath("sample/jfeature/pirates/similar-step.jfeature"), SimilarStepBugStepDefWithFailure.class));
    assertTrue(easyCucumberException.getMessage().contains("Step definition not found for: AndStep(stepString='Player3' gets disqualified)"));
  }
}
