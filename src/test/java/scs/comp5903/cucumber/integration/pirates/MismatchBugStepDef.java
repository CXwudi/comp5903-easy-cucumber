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
public class MismatchBugStepDef {

  private static final Logger log = getLogger(MismatchBugStepDef.class);
  
  @JGivenStep("The game starts with {int} player")
  public void theGameStartsWithPlayer(int numPlayers){
    log.debug("The game starts with " + numPlayers + " player");
  }

  @JAndStep("The player names are the following {string}")
  public void thePlayerNamesAreTheFollowing(String nameStr){
    log.debug("The player names are the following " + nameStr);
  }

  @JWhenStep("{string} gets {string} fortune card")
  public void playerGetsFortuneCard(String playerName, String card) {
    log.debug(playerName + " gets " + card + " fortune card");
  }

  @JAndStep("{string} rolls the following {string}")
  public void playerRollsTheFollowing(String playerName, String rollStr) {
    log.debug(playerName + " rolls the following " + rollStr);
  }

  @JAndStep("Player scores are the following {string}")
  public void playerScoresAreTheFollowing(String expectedStrScores) {
    log.debug("Player scores are the following " + expectedStrScores);
  }

  @JThenStep("{string} gets disqualified")
  public void playerGetsDisqualified(String playerName) {
    log.debug(playerName + " gets disqualified");
  }
}
