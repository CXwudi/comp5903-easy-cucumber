package scs.comp5903.cucumber.integration.rummikub;

import org.junit.jupiter.api.Test;
import scs.comp5903.cucumber.EasyCucumber;
import scs.comp5903.cucumber.util.ResourceUtil;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-23
 */
class RummikubEasyCucumberHooksTest {

  @Test
  void canDoHookCount() throws URISyntaxException, InvocationTargetException, IllegalAccessException {
    var hook = new RummikubCounterHooks();
    var stepDefs = new RummikubDummyJStepDefsWithAlternativeAnnotation();
    var jFeatureFile = ResourceUtil.getResourcePath("sample/jfeature/rummikub/initial_points.jfeature");
    EasyCucumber.build(jFeatureFile, hook, stepDefs).executeAll();
    assertEquals(1, hook.getBeforeAllJScenariosCount());
    assertEquals(1, hook.getAfterAllJScenariosCount());
    assertEquals(18, hook.getBeforeEachJScenarioCount());
    assertEquals(18, hook.getAfterEachJScenarioCount());
    assertEquals(103, hook.getBeforeEachJStepCount());
    assertEquals(103, hook.getAfterEachJStepCount());
  }
}
