package scs.comp5903.cucumber.model.jfeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-18
 */
public class JFeatureDetail {
  private final String title;
  /**
   * the tags on the JFeatureDetail has nothing to do with this easy cucumber tool </br>
   * it is just here give users the convenience to manage a collection of JFeatureDetail
   */
  private final List<String> tags;
  private final List<JScenarioDetail> scenarios;
  private final List<JScenarioOutlineDetail> scenarioOutlines;

  /**
   * these two list indicate the overall orders of all scenarios and scenario outlines <br/>
   * so that when creating and executing the actual JFeature, we know which scenario/scenario outline goes first
   */
  private final List<Integer> scenarioOrders;
  private final List<Integer> scenarioOutlineOrders;


  public JFeatureDetail(String title, List<String> tags, List<JScenarioDetail> scenarios, List<JScenarioOutlineDetail> scenarioOutlines, List<Integer> scenarioOrders, List<Integer> scenarioOutlineOrders) {
    this.title = title;
    this.tags = tags;
    this.scenarios = scenarios;
    this.scenarioOutlines = scenarioOutlines;
    this.scenarioOrders = scenarioOrders;
    this.scenarioOutlineOrders = scenarioOutlineOrders;
  }

  public static JFeatureDetailBuilder builder() {
    return new JFeatureDetailBuilder();
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

  public List<JScenarioOutlineDetail> getScenarioOutlines() {
    return this.scenarioOutlines;
  }

  public List<Integer> getScenarioOrders() {
    return this.scenarioOrders;
  }

  public List<Integer> getScenarioOutlineOrders() {
    return this.scenarioOutlineOrders;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JFeatureDetail)) {
      return false;
    }
    JFeatureDetail that = (JFeatureDetail) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  @Override
  public String toString() {
    return "JFeatureDetail(title=" + this.getTitle() + ", tags=" + this.getTags() + ")";
  }

  public static class JFeatureDetailBuilder {
    private List<Integer> scenarioOrders = new ArrayList<>();
    private List<Integer> scenarioOutlineOrders = new ArrayList<>();
    private List<JScenarioOutlineDetail> scenarioOutlines = new ArrayList<>();
    private List<JScenarioDetail> scenarios = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String title = "";

    JFeatureDetailBuilder() {
    }

    public JFeatureDetailBuilder title(String title) {
      this.title = title;
      return this;
    }

    public JFeatureDetailBuilder tags(List<String> tags) {
      this.tags = tags;
      return this;
    }

    public JFeatureDetailBuilder scenarios(List<JScenarioDetail> scenarios) {
      this.scenarios = new ArrayList<>(scenarios);
      return this;
    }

    public JFeatureDetailBuilder addScenario(JScenarioDetail scenario) {
      this.scenarios.add(scenario);
      return this;
    }

    public JFeatureDetailBuilder clearScenarios() {
      this.scenarios.clear();
      return this;
    }

    public JFeatureDetailBuilder scenarioOutlines(List<JScenarioOutlineDetail> scenarioOutlines) {
      this.scenarioOutlines = new ArrayList<>(scenarioOutlines);
      return this;
    }

    public JFeatureDetailBuilder addScenarioOutline(JScenarioOutlineDetail scenarioOutline) {
      this.scenarioOutlines.add(scenarioOutline);
      return this;
    }

    public JFeatureDetailBuilder clearScenarioOutlines() {
      this.scenarioOutlines.clear();
      return this;
    }

    public JFeatureDetailBuilder scenarioOrders(List<Integer> scenarioOrders) {
      this.scenarioOrders = new ArrayList<>(scenarioOrders);
      return this;
    }

    public JFeatureDetailBuilder addScenarioOrder(Integer scenarioOrder) {
      this.scenarioOrders.add(scenarioOrder);
      return this;
    }

    public JFeatureDetailBuilder clearScenarioOrders() {
      this.scenarioOrders.clear();
      return this;
    }

    public JFeatureDetailBuilder scenarioOutlineOrders(List<Integer> scenarioOutlineOrders) {
      this.scenarioOutlineOrders = new ArrayList<>(scenarioOutlineOrders);
      return this;
    }

    public JFeatureDetailBuilder addScenarioOutlineOrder(Integer scenarioOutlineOrder) {
      this.scenarioOutlineOrders.add(scenarioOutlineOrder);
      return this;
    }

    public JFeatureDetailBuilder clearScenarioOutlineOrders() {
      this.scenarioOutlineOrders.clear();
      return this;
    }

    public JFeatureDetail build() {
      return new JFeatureDetail(title, tags, scenarios, scenarioOutlines, scenarioOrders, scenarioOutlineOrders);
    }

    public String toString() {
      return "JFeatureDetail.JFeatureDetailBuilder(title=" + this.title + ", tags=" + this.tags + ", scenarios=" + this.scenarios + ", scenarioOutlines=" + this.scenarioOutlines + ", scenarioOrders=" + this.scenarioOrders + ", scenarioOutlineOrders=" + this.scenarioOutlineOrders + ")";
    }
  }
}
