package scs.comp5903.cucumber.sample;

import scs.comp5903.cucumber.model.JStepKeyword;
import scs.comp5903.cucumber.model.annotation.JStep;

public class TagFilteringStepDef {

  private boolean scenario1Ran = false;
  private boolean scenario2Ran = false;
  private boolean scenario3Ran = false;

  public boolean isScenario1Ran() {
    return scenario1Ran;
  }

  public boolean isScenario2Ran() {
    return scenario2Ran;
  }

  public boolean isScenario3Ran() {
    return scenario3Ran;
  }

  @JStep(keyword = JStepKeyword.GIVEN, value = "a scenario 1")
  public void scenario1() {
    scenario1Ran = true;
  }

  @JStep(keyword = JStepKeyword.GIVEN, value = "a scenario 2")
  public void scenario2() {
    scenario2Ran = true;
  }

  @JStep(keyword = JStepKeyword.GIVEN, value = "a scenario 3")
  public void scenario3() {
    scenario3Ran = true;
  }

  @JStep(keyword = JStepKeyword.WHEN, value = "a step")
  public void step() {
    // do nothing
  }

  @JStep(keyword = JStepKeyword.THEN, value = "a step")
  public void step2() {
    // do nothing
  }
}
