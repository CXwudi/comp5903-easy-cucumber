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

  public JScenario(String title, List<String> tags, List<JStepDefMethodExecution> steps) {
    this.title = title;
    this.tags = tags;
    this.steps = steps;
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

  /**
   * actual execute the scenario
   */
  public void execute() throws InvocationTargetException, IllegalAccessException {
    log.info("Executing scenario: {}", title);
    for (JStepDefMethodExecution step : steps) {
      step.execute();
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
