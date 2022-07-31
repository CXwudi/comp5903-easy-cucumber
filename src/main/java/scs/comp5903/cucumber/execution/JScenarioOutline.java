package scs.comp5903.cucumber.execution;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public class JScenarioOutline {
  private final String title;
  private final List<String> tags;
  private final List<JScenario> extractedScenarios;

  public JScenarioOutline(String title, List<String> tags, List<JScenario> extractedScenarios) {
    this.title = title;
    this.tags = tags;
    this.extractedScenarios = extractedScenarios;
  }

  public String getTitle() {
    return title;
  }

  public List<String> getTags() {
    return tags;
  }

  public List<JScenario> getExtractedScenarios() {
    return extractedScenarios;
  }

  public void execute() throws InvocationTargetException, IllegalAccessException {
    for (JScenario scenario : extractedScenarios) {
      scenario.execute();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JScenarioOutline)) {
      return false;
    }
    JScenarioOutline that = (JScenarioOutline) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  @Override
  public String toString() {
    return "JScenarioOutline(title=" + this.getTitle() + ", tags=" + this.getTags() + ")";
  }
}
