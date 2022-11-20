package scs.comp5903.cucumber.parser.samplestepdef;

import scs.comp5903.cucumber.execution.JScenarioStatus;
import scs.comp5903.cucumber.model.annotation.hook.AfterAllJScenarios;
import scs.comp5903.cucumber.model.annotation.hook.BeforeAllJScenarios;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-20
 */
public class SampleInvalidHookDefClass {

  @BeforeAllJScenarios
  public void beforeAllJScenarios(JScenarioStatus status) {
    System.out.println("beforeAllJScenarios");
  }

  @AfterAllJScenarios
  public void afterAllJScenarios(JScenarioStatus status) {
    System.out.println("afterAllJScenarios");
  }
}
