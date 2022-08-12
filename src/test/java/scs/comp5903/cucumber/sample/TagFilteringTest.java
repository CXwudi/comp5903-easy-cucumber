package scs.comp5903.cucumber.sample;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.execution.tag.BaseFilteringTag;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static scs.comp5903.cucumber.execution.tag.BaseFilteringTag.*;

/**
 * @author CX无敌
 * @date 2022-08-11
 */
class TagFilteringTest {

  @Test
  void canSeeTagOnJFeature() {
    assertDoesNotThrow(() -> {
      var jFeature = EasyCucumber.build(Path.of("src/test/resources/sample/jfeature/sample-tagged-scenarios.jfeature"), TagFilteringStepDef.class);
      assertEquals(1, jFeature.getTags().size());
      assertEquals("root", jFeature.getTags().get(0));
    });
  }

  @Test
  void shouldRunScenario1and2() {
    assertDoesNotThrow(() -> {
      var stepDefInstance = new TagFilteringStepDef();
      var jFeature = EasyCucumber.build(Path.of("src/test/resources/sample/jfeature/sample-tagged-scenarios.jfeature"), stepDefInstance);
      jFeature.executeByTag(tag("s2"));
      assertTrue(stepDefInstance.isScenario1Ran());
      assertTrue(stepDefInstance.isScenario2Ran());
      assertFalse(stepDefInstance.isScenario3Ran());
    });
    assertDoesNotThrow(() -> {
      var stepDefInstance = new TagFilteringStepDef();
      var jFeature = EasyCucumber.build(Path.of("src/test/resources/sample/jfeature/sample-tagged-scenarios.jfeature"), stepDefInstance);
      jFeature.executeByTag(creatXor("s1", "s3"));
      assertTrue(stepDefInstance.isScenario1Ran());
      assertTrue(stepDefInstance.isScenario2Ran());
      assertFalse(stepDefInstance.isScenario3Ran());
    });
  }

  @Test
  void shouldRunScenario2and3() {
    assertDoesNotThrow(() -> {
      var stepDefInstance = new TagFilteringStepDef();
      var jFeature = EasyCucumber.build(Path.of("src/test/resources/sample/jfeature/sample-tagged-scenarios.jfeature"), stepDefInstance);
      jFeature.executeByTag(tag("s3"));
      assertFalse(stepDefInstance.isScenario1Ran());
      assertTrue(stepDefInstance.isScenario2Ran());
      assertTrue(stepDefInstance.isScenario3Ran());
    });
    assertDoesNotThrow(() -> {
      var stepDefInstance = new TagFilteringStepDef();
      var jFeature = EasyCucumber.build(Path.of("src/test/resources/sample/jfeature/sample-tagged-scenarios.jfeature"), stepDefInstance);
      jFeature.executeByTag(creatXor("s1", "s2"));
      assertFalse(stepDefInstance.isScenario1Ran());
      assertTrue(stepDefInstance.isScenario2Ran());
      assertTrue(stepDefInstance.isScenario3Ran());
    });
  }

  private BaseFilteringTag creatXor(String tag1Str, String tag2Str) {
    return or(and(tag(tag1Str), not(tag(tag2Str))), and(not(tag(tag1Str)), tag(tag2Str)));
  }
}

