package scs.comp5903.cucumber.integration.pirates;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.step.JGivenStep;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Charles Chen 101035684
 * @date 2023-04-01
 */
public class TwoClassWithSameMethodNamesStepDef {
  public static class Class1 {
    private static final Logger log = getLogger(Class1.class);
    @JGivenStep("Test given 1")
    public void method1() {
      log.info("Test given 1");
    }
  }

  public static class Class2 {
    private static final Logger log = getLogger(Class2.class);
    @JGivenStep("Test given 2")
    public void method1() {
      log.info("Test given 2");
    }
  }
}
