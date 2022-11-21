package scs.comp5903.cucumber.parser.samplestepdef;

import scs.comp5903.cucumber.execution.JScenarioStatus;
import scs.comp5903.cucumber.model.annotation.hook.AfterAllJScenarios;
import scs.comp5903.cucumber.model.annotation.hook.BeforeAllJScenarios;
import scs.comp5903.cucumber.model.annotation.hook.BeforeEachJScenario;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-20
 */
public class SampleInvalidHookDefClass {
  public static class Sample1 {
    @BeforeAllJScenarios
    public void beforeAllJScenarios(JScenarioStatus status) {
      System.out.println("beforeAllJScenarios");
    }
  }

  public static class Sample2 {
    @AfterAllJScenarios
    public void afterAllJScenarios(JScenarioStatus status) {
      System.out.println("afterAllJScenarios");
    }
  }

  public static class Sample3 {
    @BeforeEachJScenario
    public void beforeEachJScenarios(String a, int b) {
      System.out.println("beforeAllJScenarios");
    }
  }

  public static class Sample4 {
    @BeforeEachJScenario
    public void beforeEachJScenarios(String a) {
      System.out.println("beforeAllJScenarios");
    }
  }
}
