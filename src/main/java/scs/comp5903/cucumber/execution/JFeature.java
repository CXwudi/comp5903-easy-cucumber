package scs.comp5903.cucumber.execution;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2022-06-23
 */
public class JFeature {

  private static final Logger log = getLogger(JFeature.class);

  private final String title;
  private final List<String> tags;
  private final List<JScenario> scenarios;
  private final List<JScenarioOutline> scenarioOutlines;

  /**
   * similar to {@link scs.comp5903.cucumber.model.JFeatureDetail}'s order list, we need to remember which goes first
   */
  private final List<Integer> scenarioOrders;
  private final List<Integer> scenarioOutlineOrders;

  public JFeature(String title, List<String> tags, List<JScenario> scenarios, List<JScenarioOutline> scenarioOutlines, List<Integer> scenarioOrders, List<Integer> scenarioOutlineOrders) {
    this.title = title;
    this.tags = tags;
    this.scenarios = scenarios;
    this.scenarioOutlines = scenarioOutlines;
    this.scenarioOrders = scenarioOrders;
    this.scenarioOutlineOrders = scenarioOutlineOrders;
    if (scenarioOrders.size() != scenarios.size()) {
      throw new EasyCucumberException(ErrorCode.EZCU005, "scenarioOrders.size() != scenarios.size()");
    }
    if (scenarioOutlineOrders.size() != scenarioOutlines.size()) {
      throw new EasyCucumberException(ErrorCode.EZCU006, "scenarioOutlineOrders.size() != scenarioOutlines.size()");
    }
  }

  public String getTitle() {
    return title;
  }

  public List<String> getTags() {
    return tags;
  }

  public List<JScenario> getScenarios() {
    return scenarios;
  }

  public List<JScenarioOutline> getScenarioOutlines() {
    return scenarioOutlines;
  }

  /**
   * execute all scenarios in this feature with respect to the order
   */
  public void executeAll() throws InvocationTargetException, IllegalAccessException {
    log.info("Start executing the feature: {}", title);
    HashMap<Integer, JScenario> orderToScenarioMap = new HashMap<>(scenarios.size());
    for (int i = 0; i < scenarios.size(); i++) {
      orderToScenarioMap.put(scenarioOrders.get(i), scenarios.get(i));
    }
    HashMap<Integer, JScenarioOutline> orderToScenarioOutlineMap = new HashMap<>(scenarios.size());
    for (int i = 0; i < scenarioOutlines.size(); i++) {
      orderToScenarioOutlineMap.put(scenarioOutlineOrders.get(i), scenarioOutlines.get(i));
    }
    for (int i = 0; i < scenarios.size() + scenarioOutlines.size(); i++) {
      if (orderToScenarioMap.containsKey(i)) {
        orderToScenarioMap.get(i).execute();
      } else {
        orderToScenarioOutlineMap.get(i).execute();
      }
    }
    log.info("Done executing the feature: {}", title);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JFeature)) {
      return false;
    }
    JFeature that = (JFeature) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }

  @Override
  public String toString() {
    return "JFeature(title=" + this.getTitle() + ", tags=" + this.getTags() + ")";
  }
}
