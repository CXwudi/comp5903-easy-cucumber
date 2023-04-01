package scs.comp5903.cucumber.integration.pirates;

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

  @Test
  void checkIssue25() {
    // this test requires to manually rerun it several times, as each time needs a new JVM
    assertDoesNotThrow(() -> EasyCucumber.build(ResourceUtil.getResourcePath("sample/jfeature/pirates/similar-step-2.jfeature"), SimilarStepBugStepDef2.class));
  }

  @Test
  void checkIssueFromVictor() {
    assertDoesNotThrow(() -> EasyCucumber.build(ResourceUtil.getResourcePath("sample/jfeature/pirates/same-method-name-in-2-classes.jfeature"),
        TwoClassWithSameMethodNamesStepDef.Class1.class, TwoClassWithSameMethodNamesStepDef.Class2.class).executeAll());
  }
}
