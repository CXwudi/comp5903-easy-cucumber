package scs.comp5903.cucumber.sample;

import org.slf4j.Logger;
import scs.comp5903.cucumber.model.annotation.step.JStep;

import static scs.comp5903.cucumber.model.annotation.step.JStepKeyword.*;

/**
 * this class is exactly same as {@link RummikubDummyJStepDefs} but with different method name
 * that doesn't have any pattern related to the step definition <br/>
 * <p>
 * To show feature 3d is fixed
 */
public class RummikubDummyJStepDefsWithRandomMethodName {
  private final static Logger log = org.slf4j.LoggerFactory.getLogger(RummikubDummyJStepDefsWithRandomMethodName.class);

  @JStep(keyword = GIVEN, value = "Test Server is started")
  public void method_a() {
    log.debug("Given Test Server is started");
  }

  @JStep(keyword = GIVEN, value = "Player {int} hand starts with {string}")
  public void method_b(int pNum, String tiles) {
    log.debug("Given Player {} hand starts with {}", pNum, tiles);
  }

  @JStep(keyword = GIVEN, value = "Player {int} has played initial points")
  public void method_c(int pNum) {
    log.debug("Given Player {} has played initial points", pNum);
  }

  @JStep(keyword = WHEN, value = "Player {int} draws {string}")
  public void method_d(int pNum, String tile) {
    log.debug("When Player {} draws {}", pNum, tile);
  }

  @JStep(keyword = WHEN, value = "Player {int} chooses to draw")
  public void method_e(int pNum) {
    log.debug("When Player {} chooses to draw", pNum);
  }

  @JStep(keyword = WHEN, value = "Player {int} has to draw")
  public void method_f(int pNum) {
    log.debug("When Player {} has to draw", pNum);
  }

  @JStep(keyword = WHEN, value = "Player {int} plays {string}")
  public void method_g(int pNum, String tiles) {
    log.debug("When Player {} plays {}", pNum, tiles);
  }

  @JStep(keyword = THEN, value = "table contains {string}")
  public void method_h(String expected) {
    log.debug("Then table contains {}", expected);
  }

  @JStep(keyword = THEN, value = "Player {int} hand contains {string}")
  public void method_i(int pNum, String expected) {
    log.debug("Then Player {} hand contains {}", pNum, expected);
  }

  @JStep(keyword = THEN, value = "Player {int} wins")
  public void method_j(int pNum) {
    log.debug("Then Player {} wins", pNum);
  }

  @JStep(keyword = THEN, value = "Player {int} score is {int}")
  public void method_k(int pNum, int score) {
    log.debug("Then Player {} score is {}", pNum, score);
  }

  // belows are same step defs but with and keywords, not all step defs below are used


  @JStep(keyword = AND, value = "Player {int} hand starts with {string}")
  public void method_l(int pNum, String tiles) {
    log.debug("And Player {} hand starts with {}", pNum, tiles);
  }

  @JStep(keyword = AND, value = "Player {int} has played initial points")
  public void method_m(int pNum) {
    log.debug("And Player {} has played initial points", pNum);
  }

  @JStep(keyword = AND, value = "Player {int} draws {string}")
  public void method_n(int pNum, String tile) {
    log.debug("And Player {} draws {}", pNum, tile);
  }

  @JStep(keyword = AND, value = "Player {int} chooses to draw")
  public void method_o(int pNum) {
    log.debug("And Player {} chooses to draw", pNum);
  }

  @JStep(keyword = AND, value = "Player {int} has to draw")
  public void method_p(int pNum) {
    log.debug("And Player {} has to draw", pNum);
  }

  @JStep(keyword = AND, value = "Player {int} plays {string}")
  public void method_q(int pNum, String tiles) {
    log.debug("And Player {} plays {}", pNum, tiles);
  }

  @JStep(keyword = AND, value = "table contains {string}")
  public void method_r(String expected) {
    log.debug("And table contains {}", expected);
  }

  @JStep(keyword = AND, value = "Player {int} hand contains {string}")
  public void method_s(int pNum, String expected) {
    log.debug("And Player {} hand contains {}", pNum, expected);
  }

  @JStep(keyword = AND, value = "Player {int} wins")
  public void method_t(int pNum) {
    log.debug("And Player {} wins", pNum);
  }

  @JStep(keyword = AND, value = "Player {int} score is {int}")
  public void method_u(int pNum, int score) {
    log.debug("And Player {} score is {}", pNum, score);
  }
}

