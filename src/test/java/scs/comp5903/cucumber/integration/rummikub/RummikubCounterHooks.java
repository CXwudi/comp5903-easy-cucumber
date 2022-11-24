package scs.comp5903.cucumber.integration.rummikub;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.hook.*;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-23
 */
public class RummikubCounterHooks {

  private final static Logger log = org.slf4j.LoggerFactory.getLogger(RummikubCounterHooks.class);
  private int beforeAllJScenariosCount = 0;
  private int afterAllJScenariosCount = 0;
  private int beforeEachJScenarioCount = 0;
  private int afterEachJScenarioCount = 0;
  private int beforeEachJStepCount = 0;
  private int afterEachJStepCount = 0;


  @BeforeAllJScenarios
  public void beforeAllJScenarios() {
    log.debug("beforeAllJScenarios");
    beforeAllJScenariosCount++;
  }

  @AfterAllJScenarios
  public void afterAllJScenarios() {
    log.debug("afterAllJScenarios");
    afterAllJScenariosCount++;
  }

  @BeforeEachJScenario
  public void beforeEachJScenario() {
    log.debug("beforeEachJScenario");
    beforeEachJScenarioCount++;
  }

  @AfterEachJScenario
  public void afterEachJScenario() {
    log.debug("afterEachJScenario");
    afterEachJScenarioCount++;
  }

  @BeforeEachJStep
  public void beforeEachJStep() {
    log.debug("beforeEachJStep");
    beforeEachJStepCount++;
  }

  @AfterEachJStep
  public void afterEachJStep() {
    log.debug("afterEachJStep");
    afterEachJStepCount++;
  }

  public int getBeforeAllJScenariosCount() {
    return beforeAllJScenariosCount;
  }

  public int getAfterAllJScenariosCount() {
    return afterAllJScenariosCount;
  }

  public int getBeforeEachJScenarioCount() {
    return beforeEachJScenarioCount;
  }

  public int getAfterEachJScenarioCount() {
    return afterEachJScenarioCount;
  }

  public int getBeforeEachJStepCount() {
    return beforeEachJStepCount;
  }

  public int getAfterEachJStepCount() {
    return afterEachJStepCount;
  }
}
