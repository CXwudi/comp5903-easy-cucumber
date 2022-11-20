package scs.comp5903.cucumber.parser.samplestepdef;

import scs.comp5903.cucumber.model.annotation.hook.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-19
 */
public class AnotherSampleHookDefinition {

  @BeforeAllJScenarios(order = 7)
  public void beforeAllJScenarios() {
  }

  @AfterAllJScenarios(order = 8)
  public void afterAllJScenarios() {
  }

  @BeforeEachJScenario(order = 9)
  public void beforeEachJScenario() {
  }

  @AfterEachJScenario(order = 10)
  public void afterEachJScenario() {
  }

  @BeforeEachJStep(order = 11)
  public void beforeEachJStep() {
  }

  @AfterEachJStep(order = 12)
  public void afterEachJStep() {
  }
}
