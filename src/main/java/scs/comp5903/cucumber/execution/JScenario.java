package scs.comp5903.cucumber.execution;

import org.slf4j.Logger;
import scs.comp5903.cucumber.execution.tag.BaseFilteringTag;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public class JScenario implements TagsContainer {
  private static final Logger log = getLogger(JScenario.class);
  private final String title;
  private final List<String> tags;
  private final List<JStepDefMethodExecution> steps;
  private final List<JHookMethodExecution> beforeScenarioHooks;
  private final List<JHookMethodExecution> afterScenarioHooks;
  private final List<JHookMethodExecution> beforeStepHooks;
  private final List<JHookMethodExecution> afterStepHooks;

  public JScenario(
      String title,
      List<String> tags,
      List<JStepDefMethodExecution> steps,
      List<JHookMethodExecution> beforeScenarioHooks,
      List<JHookMethodExecution> afterScenarioHooks,
      List<JHookMethodExecution> beforeStepHooks,
      List<JHookMethodExecution> afterStepHooks) {
    this.title = title;
    this.tags = tags;
    this.steps = steps;
    this.beforeScenarioHooks = beforeScenarioHooks;
    this.afterScenarioHooks = afterScenarioHooks;
    this.beforeStepHooks = beforeStepHooks;
    this.afterStepHooks = afterStepHooks;
  }

  public String getTitle() {
    return title;
  }

  /**
   * get tags placed right above the "Scenario" keyword
   */
  @Override
  public List<String> getTags() {
    return tags;
  }

  public List<JStepDefMethodExecution> getSteps() {
    return steps;
  }

  public List<JHookMethodExecution> getBeforeScenarioHooks() {
    return beforeScenarioHooks;
  }

  public List<JHookMethodExecution> getAfterScenarioHooks() {
    return afterScenarioHooks;
  }

  public List<JHookMethodExecution> getBeforeStepHooks() {
    return beforeStepHooks;
  }

  public List<JHookMethodExecution> getAfterStepHooks() {
    return afterStepHooks;
  }

  /**
   * actual execute the scenario
   */
  public void execute() throws InvocationTargetException, IllegalAccessException {
    log.info("Executing scenario: {}", title);
    var status = new JScenarioStatus(this);
    executeBeforeScenarioHooks(status);
    try {
      for (JStepDefMethodExecution step : steps) {
        executeBeforeStepHooks(status);
        try {
          step.execute();
        } finally {
          status.incrementAndReturnStepIndex();
          executeAfterStepHooks(status);
        }
      }
    } catch (Throwable t) {
      log.error("Error occurred during scenario execution: {}", title, t);
      throw t;
    } finally {
      executeAfterScenarioHooks(status);
    }
  }

  private void executeBeforeScenarioHooks(JScenarioStatus status) throws InvocationTargetException, IllegalAccessException {
    for (JHookMethodExecution hook : beforeScenarioHooks) {
      hook.executeOnParametersMatch(status);
    }
  }

  private void executeBeforeStepHooks(JScenarioStatus status) throws InvocationTargetException, IllegalAccessException {
    for (JHookMethodExecution hook : beforeStepHooks) {
      hook.executeOnParametersMatch(status);
    }
  }

  private void executeAfterStepHooks(JScenarioStatus status) throws InvocationTargetException, IllegalAccessException {
    for (JHookMethodExecution hook : afterStepHooks) {
      hook.executeOnParametersMatch(status);
    }
  }

  private void executeAfterScenarioHooks(JScenarioStatus status) throws InvocationTargetException, IllegalAccessException {
    for (JHookMethodExecution hook : afterScenarioHooks) {
      hook.executeOnParametersMatch(status);
    }
  }

  public void executeConditionallyBy(BaseFilteringTag tag) throws InvocationTargetException, IllegalAccessException {
    if (tag.isTagMatch(this)) {
      execute();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JScenario)) {
      return false;
    }
    JScenario that = (JScenario) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  @Override
  public String toString() {
    return "JScenario(title=" + title + ", tags=" + tags + ")";
  }
}
