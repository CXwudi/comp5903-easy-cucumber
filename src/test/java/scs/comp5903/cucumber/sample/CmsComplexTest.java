package scs.comp5903.cucumber.sample;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.util.ResourceUtil;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-27
 */
@Disabled("Disabled for CI, uncomment this line to run this test locally")
class CmsComplexTest {

  @Test
  void complexTestThroughMultithreadStepDef() {
    assertDoesNotThrow(() -> {
      var jFeatureFile = ResourceUtil.getResourcePath("sample/jfeature/cms/two_students_race_for_one_spot.jfeature");
      EasyCucumber.build(jFeatureFile, CmsComplexScenarioMultithreadStepDefs.class).execute();
    });
  }

  @Test
  void complexTestThroughMultipleScenarios() {
    assertDoesNotThrow(() -> {
      var jFeatureFile = ResourceUtil.getResourcePath("sample/jfeature/cms/two_students_race_for_one_spot.jfeature");
      EasyCucumber.build(jFeatureFile, CmsComplexScenarioMultiScenarioStepDefs.class).execute();
    });
  }

}
