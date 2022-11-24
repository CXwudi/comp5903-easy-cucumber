package scs.comp5903.cucumber.integration.pirates;

import scs.comp5903.cucumber.model.annotation.step.JAndStep;

/**
 * @author Charles Chen 101035684
 * @date 2022-11-24
 */
public class SimilarStepBugStepDef2 {

  @JAndStep("Player rerolls {int} {string} and gets {int} {string}")
  public void and_playerRerollsAndGets(int roll0, String rollS0, int get0, String getS0) {

  }

  @JAndStep("Player rerolls {int} {string} and gets {int} {string} and {int} {string}")
  public void and_playerRerollsAndGetsAnd(int roll0, String rollS0, int get0, String getS0, int get1, String getS1) {

  }

  @JAndStep("Player rerolls {int} {string} and gets {int} {string} and {int} {string} and {int} {string}")
  public void and_playerRerollsAndGetsAndAnd(int roll0, String rollS0, int get0, String getS0, int get1, String getS1, int get2, String getS2){

  }
}
