package scs.comp5903.cucumber.sample.pirates;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.JAndStep;
import scs.comp5903.cucumber.model.annotation.JGivenStep;
import scs.comp5903.cucumber.model.annotation.JThenStep;
import scs.comp5903.cucumber.model.annotation.JWhenStep;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Similar to {@link SimilarStepBugStepDef}, but with a mismatch in the {@link #disqualified(String)} method.
 * @author CX无敌
 * @date 2022-10-26
 */
public class SimilarStepBugStepDefWithFailure {
  private static final Logger log = getLogger(SimilarStepBugStepDefWithFailure.class);
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

  @JAndStep("{string} is disqualified")
  public void disqualified(String player) {
    log.debug(player + " is disqualified");
  }
}
