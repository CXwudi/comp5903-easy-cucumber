package scs.comp5903.cucumber.integration.pirates;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.step.JAndStep;
import scs.comp5903.cucumber.model.annotation.step.JGivenStep;
import scs.comp5903.cucumber.model.annotation.step.JThenStep;
import scs.comp5903.cucumber.model.annotation.step.JWhenStep;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author CX无敌
 * @date 2022-10-26
 */
public class SimilarStepBugStepDef {
  private static final Logger log = getLogger(SimilarStepBugStepDef.class);
  @JGivenStep("Test given")
  public void given() {
    log.debug("Test given");
  }

  @JWhenStep("Test when")
  public void when() {
    log.debug("Test when");
  }

  @JThenStep("Test then")
  public void then() {
    log.debug("Test then");
  }

  @JAndStep("{string} gets {string} fortune card")
  public void andTest(String player, String card) {
    log.debug(player + " gets " + card + " fortune card");
  }

  @JAndStep("{string} gets disqualified")
  public void disqualified(String player) {
    log.debug(player + " gets disqualified");
  }
}
