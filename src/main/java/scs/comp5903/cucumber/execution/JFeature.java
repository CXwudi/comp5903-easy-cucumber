package scs.comp5903.cucumber.execution;

import org.slf4j.Logger;
import scs.comp5903.cucumber.execution.tag.AlwaysTrueTag;
import scs.comp5903.cucumber.execution.tag.BaseFilteringTag;
import scs.comp5903.cucumber.model.exception.EasyCucumberException;
import scs.comp5903.cucumber.model.exception.ErrorCode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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

  /**
   * save the order and scenario/scenario outline as a map for convenience
   */
  private final HashMap<Integer, JScenario> orderToScenarioMap;
  private final HashMap<Integer, JScenarioOutline> orderToScenarioOutlineMap;
  private final List<JHookMethodExecution> beforeAllScenariosHooks;
  private final List<JHookMethodExecution> afterAllScenariosHooks;

  public JFeature(String title,
                  List<String> tags,
                  List<JScenario> scenarios,
                  List<JScenarioOutline> scenarioOutlines,
                  List<Integer> scenarioOrders,
                  List<Integer> scenarioOutlineOrders,
                  List<JHookMethodExecution> beforeAllScenariosHooks,
                  List<JHookMethodExecution> afterAllScenariosHooks) {
    this.title = title;
    this.tags = tags;
    this.beforeAllScenariosHooks = beforeAllScenariosHooks;
    this.afterAllScenariosHooks = afterAllScenariosHooks;
    if (scenarioOrders.size() != scenarios.size()) {
      throw new EasyCucumberException(ErrorCode.EZCU005, "scenarioOrders.size() != scenarios.size()");
    }
    if (scenarioOutlineOrders.size() != scenarioOutlines.size()) {
      throw new EasyCucumberException(ErrorCode.EZCU006, "scenarioOutlineOrders.size() != scenarioOutlines.size()");
    }
    orderToScenarioMap = new HashMap<>(scenarios.size());
    orderToScenarioOutlineMap = new HashMap<>(scenarios.size());
    buildOrderMaps(scenarios, scenarioOrders, scenarioOutlines, scenarioOutlineOrders);
  }

  private void buildOrderMaps(List<JScenario> scenarios, List<Integer> scenarioOrders, List<JScenarioOutline> scenarioOutlines, List<Integer> scenarioOutlineOrders) {
    for (int i = 0; i < scenarios.size(); i++) {
      orderToScenarioMap.put(scenarioOrders.get(i), scenarios.get(i));
    }
    for (int i = 0; i < scenarioOutlines.size(); i++) {
      orderToScenarioOutlineMap.put(scenarioOutlineOrders.get(i), scenarioOutlines.get(i));
    }
  }

  public String getTitle() {
    return title;
  }

  /**
   * get tags placed right above the "Feature" keyword
   */
  public List<String> getTags() {
    return tags;
  }

  public List<JScenario> getScenarios() {
    return new ArrayList<>(orderToScenarioMap.values());
  }

  public List<JScenarioOutline> getScenarioOutlines() {
    return new ArrayList<>(orderToScenarioOutlineMap.values());
  }

  public List<JHookMethodExecution> getBeforeAllScenariosHooks() {
    return beforeAllScenariosHooks;
  }

  public List<JHookMethodExecution> getAfterAllScenariosHooks() {
    return afterAllScenariosHooks;
  }

  /**
   * execute all scenarios in this feature with respect to the order
   */
  public void executeAll() throws InvocationTargetException, IllegalAccessException {
    executeByTag(AlwaysTrueTag.INSTANCE);
  }

  public void executeByTag(BaseFilteringTag tag) throws InvocationTargetException, IllegalAccessException {
    log.info("Start executing the feature: '{}' by tag expression: '{}'", title, tag);
    executeBeforeAllScenariosHooks();
    int i = 0;
    try {
      for (;i < orderToScenarioMap.size() + orderToScenarioOutlineMap.size(); i++) {
        if (orderToScenarioMap.containsKey(i)) {
          orderToScenarioMap.get(i).executeConditionallyBy(tag);
        } else {
          orderToScenarioOutlineMap.get(i).executeConditionallyBy(tag);
        }
      }
    } catch(Throwable t) {
      log.error("Error occurred when executing the scenario at order {} in the feature '{}'", i + 1, title, t);
      throw t;
    } finally {
      executeAfterAllScenariosHooks();
    }
    log.info("All {} scenario(s) in the feature: '{}' passed with the tag expression: {}", i + 1, title, tag);
  }

  private void executeBeforeAllScenariosHooks() throws InvocationTargetException, IllegalAccessException {
    log.info("Start executing the before all scenarios hooks");
    for (JHookMethodExecution hook : beforeAllScenariosHooks) {
      hook.executeOnParametersMatch();
    }
  }

  private void executeAfterAllScenariosHooks() throws InvocationTargetException, IllegalAccessException {
    log.info("Start executing the after all scenarios hooks");
    for (JHookMethodExecution hook : afterAllScenariosHooks) {
      hook.executeOnParametersMatch();
    }
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
