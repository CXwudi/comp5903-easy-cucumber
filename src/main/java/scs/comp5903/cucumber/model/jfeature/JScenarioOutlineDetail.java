package scs.comp5903.cucumber.model.jfeature;

import java.util.List;
import java.util.Objects;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-18
 */
public class JScenarioOutlineDetail {
  private final String title;
  /**
   * tags on the scenario outline title </br>
   * <p>
   * while this scenario outline is basically just a collection of scenarios, and we can rely on the tags of the scenarios, </br>
   * but let's just use tags on the scenario outline to do the same thing
   */
  private final List<String> tags;
  private final List<JScenarioDetail> scenarios;

  public JScenarioOutlineDetail(String title, List<String> tags, List<JScenarioDetail> scenarios) {
    this.title = title;
    this.tags = tags;
    this.scenarios = scenarios;
  }

  public String getTitle() {
    return this.title;
  }

  public List<String> getTags() {
    return this.tags;
  }

  public List<JScenarioDetail> getScenarios() {
    return this.scenarios;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JScenarioOutlineDetail)) {
      return false;
    }
    JScenarioOutlineDetail that = (JScenarioOutlineDetail) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  @Override
  public String toString() {
    return "JScenarioOutlineDetail(title=" + this.getTitle() + ", tags=" + this.getTags() + ")";
  }
}
