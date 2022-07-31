package scs.comp5903.cucumber.model;

import scs.comp5903.cucumber.model.jstep.AbstractJStep;

import java.util.List;
import java.util.Objects;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-16
 */
public final class JScenarioDetail {
  private final String title;
  private final List<String> tags;
  private final List<AbstractJStep> steps;

  public JScenarioDetail(String title, List<String> tags, List<AbstractJStep> steps) {
    this.title = title;
    this.tags = tags;
    this.steps = steps;
  }

  public String getTitle() {
    return this.title;
  }

  public List<String> getTags() {
    return this.tags;
  }

  public List<AbstractJStep> getSteps() {
    return this.steps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JScenarioDetail)) {
      return false;
    }
    JScenarioDetail that = (JScenarioDetail) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  @Override
  public String toString() {
    return "JScenarioDetail(title=" + this.getTitle() + ", tags=" + this.getTags() + ")";
  }
}
