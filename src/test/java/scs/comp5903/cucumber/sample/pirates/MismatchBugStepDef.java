package scs.comp5903.cucumber.sample.pirates;

import scs.comp5903.cucumber.model.annotation.JAndStep;
import scs.comp5903.cucumber.model.annotation.JGivenStep;
import scs.comp5903.cucumber.model.annotation.JThenStep;
import scs.comp5903.cucumber.model.annotation.JWhenStep;

/**
 * @author CX无敌
 * @date 2022-10-26
 */
public class MismatchBugStepDef {
  @JGivenStep("The game starts with {int} player")
  public void theGameStartsWithPlayer(int numPlayers){
    System.out.println("The game starts with " + numPlayers + " player");
  }

  @JAndStep("The player names are the following {string}")
  public void thePlayerNamesAreTheFollowing(String nameStr){
    System.out.println("The player names are the following " + nameStr);
  }

  @JWhenStep("{string} gets {string} fortune card")
  public void playerGetsFortuneCard(String playerName, String card) {
    System.out.println(playerName + " gets " + card + " fortune card");
  }

  @JAndStep("{string} rolls the following {string}")
  public void playerRollsTheFollowing(String playerName, String rollStr) {
    System.out.println(playerName + " rolls the following " + rollStr);
  }

  @JAndStep("Player scores are the following {string}")
  public void playerScoresAreTheFollowing(String expectedStrScores) {
    System.out.println("Player scores are the following " + expectedStrScores);
  }

  @JThenStep("{string} gets disqualified")
  public void playerGetsDisqualified(String playerName) {
    System.out.println(playerName + " gets disqualified");
  }
}
