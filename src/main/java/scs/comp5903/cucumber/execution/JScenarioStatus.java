package scs.comp5903.cucumber.execution;

import java.util.Optional;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-19
 */
public class JScenarioStatus {

  private final JScenario jScenarioInstance;
  private int stepIndex;
  private Optional<Exception> failureOpt;

  public JScenarioStatus(JScenario jScenarioInstance) {
    this.jScenarioInstance = jScenarioInstance;
    this.stepIndex = 0;
    this.failureOpt = Optional.empty();
  }

  public JScenario getJScenarioInstance() {
    return jScenarioInstance;
  }

  public int getStepIndex() {
    return stepIndex;
  }

  public void setStepIndex(int stepIndex) {
    this.stepIndex = stepIndex;
  }

  public boolean hasFailure() {
    return failureOpt.isPresent();
  }

  public Exception getFailure() {
    return failureOpt.orElseThrow();
  }

  public void setFailure(Exception failureOpt) {
    this.failureOpt = Optional.of(failureOpt);
  }
}
