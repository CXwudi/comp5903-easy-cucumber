package scs.comp5903.cucumber.parser.samplestepdef;

import scs.comp5903.cucumber.execution.JScenarioStatus;
import scs.comp5903.cucumber.model.annotation.hook.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-19
 */
public class SampleHookDefinition {

  @BeforeAllJScenarios(order = 1)
  public void beforeAllJScenarios() {
  }

  @AfterAllJScenarios(order = 2)
  public void afterAllJScenarios() {
  }

  @BeforeEachJScenario(order = 3)
  public void beforeEachJScenario(JScenarioStatus status) {
  }

  @AfterEachJScenario(order = 4)
  public void afterEachJScenario(JScenarioStatus status) {
  }

  @BeforeEachJStep(order = 5)
  public void beforeEachJStep(JScenarioStatus status) {
  }

  @AfterEachJStep(order = 6)
  public void afterEachJStep(JScenarioStatus status) {
  }
}
