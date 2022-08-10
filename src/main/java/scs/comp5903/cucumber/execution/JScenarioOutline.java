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
public class JScenarioOutline implements TagsContainer {

  private static final Logger log = getLogger(JScenarioOutline.class);

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

  @Override
  public List<String> getTags() {
    return tags;
  }

  public List<JScenario> getExtractedScenarios() {
    return extractedScenarios;
  }

  public void execute() throws InvocationTargetException, IllegalAccessException {
    log.info("Executing scenario outline: {}", title);
    for (JScenario scenario : extractedScenarios) {
      scenario.execute();
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
