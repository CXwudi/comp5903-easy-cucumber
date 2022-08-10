package scs.comp5903.cucumber.execution;

import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public class JScenario implements JExecutable, TagsContainer {
  private static final Logger log = getLogger(JScenario.class);
  private final String title;
  private final List<String> tags;
  private final List<MethodExecution> steps;

  public JScenario(String title, List<String> tags, List<MethodExecution> steps) {
    this.title = title;
    this.tags = tags;
    this.steps = steps;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public List<String> getTags() {
    return tags;
  }

  public List<MethodExecution> getSteps() {
    return steps;
  }

  /**
   * actual execute the scenario
   */
  @Override
  public void execute() throws InvocationTargetException, IllegalAccessException {
    log.info("Executing scenario: {}", title);
    for (MethodExecution step : steps) {
      step.execute();
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
